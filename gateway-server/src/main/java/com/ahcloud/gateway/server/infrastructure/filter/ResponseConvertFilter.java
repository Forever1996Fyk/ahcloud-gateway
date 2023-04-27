package com.ahcloud.gateway.server.infrastructure.filter;

import com.ahcloud.common.utils.JsonUtils;
import com.ahcloud.gateway.client.enums.GatewayRetCodeEnum;
import com.ahcloud.gateway.core.domain.response.GatewayResponseResult;
import com.ahcloud.gateway.server.infrastructure.exception.GatewayException;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.reactivestreams.Publisher;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Nullable;
import java.nio.charset.StandardCharsets;


/**
 * @program: ahcloud-gateway
 * @description: 响应过滤器 (暂时废弃)
 * @author: YuKai Fan
 * @create: 2023/1/15 22:50
 **/
@Slf4j
@Deprecated
//@Component
public class ResponseConvertFilter implements WebFilter, Ordered {
    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE;
    }

    @Override
    public Mono<Void> filter(@Nullable ServerWebExchange exchange, @Nullable WebFilterChain chain) {
        // 原始返回体
        assert exchange != null;
        ServerHttpResponse originalResponse = exchange.getResponse();
        DataBufferFactory bufferFactory = originalResponse.bufferFactory();
        ServerHttpResponseDecorator decorator = new ServerHttpResponseDecorator(originalResponse) {
            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                if (body instanceof Flux) {
                    Flux<? extends DataBuffer> fluxBody = (Flux<? extends DataBuffer>) body;
                    try {
                        return super.writeWith(fluxBody.buffer().map(dataBuffers -> {
                            DefaultDataBufferFactory dataBufferFactory = new DefaultDataBufferFactory();
                            DataBuffer dataBuffer = dataBufferFactory.join(dataBuffers);
                            byte[] content = new byte[dataBuffer.readableByteCount()];
                            dataBuffer.read(content);
                            // 释放内存
                            DataBufferUtils.release(dataBuffer);
                            String responseData = StringUtils.toEncodedString(content, StandardCharsets.UTF_8);
                            GatewayResponseResult result = convert(responseData);
                            byte[] uppedContent = JsonUtils.beanToByte(result);
                            return bufferFactory.wrap(uppedContent);
                        }));
                    } catch (Exception e) {
                        log.error("ResponseConvertFilter[filter] response body convert to GatewayResponseResult failed, reason is {}", Throwables.getStackTraceAsString(e));
                        throw new GatewayException(GatewayRetCodeEnum.RESPONSE_BODY_CONVERT_FAILED);
                    }
                }
                return super.writeWith(body);
            }
        };
        assert chain != null;
        return chain.filter(exchange.mutate().response(decorator).build());
    }

    private GatewayResponseResult convert(String resData) {
        try {
            if (StringUtils.isNotBlank(resData)) {
                Object o = JsonUtils.stringToBean(resData, Object.class);
                return GatewayResponseResult.ofSuccess(o);
            } else {
                return GatewayResponseResult.ofSuccess();
            }
        } catch (Exception e) {
            return GatewayResponseResult.ofFailed();
        }
    }
}
