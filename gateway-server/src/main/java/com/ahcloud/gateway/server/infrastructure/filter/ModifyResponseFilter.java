package com.ahcloud.gateway.server.infrastructure.filter;

import com.ahcloud.common.utils.JsonUtils;
import com.ahcloud.gateway.client.exception.GatewayException;
import com.ahcloud.gateway.core.domain.response.GatewayResponseResult;
import com.ahcloud.gateway.scg.common.util.ResponseUtils;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.reactivestreams.Publisher;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
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
public class ModifyResponseFilter implements WebFilter, Ordered {
    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE;
    }

    @Override
    public Mono<Void> filter(@Nullable ServerWebExchange exchange, @Nullable WebFilterChain chain) {
        // 原始返回体
        assert exchange != null;
        assert chain != null;
        return chain.filter(exchange.mutate().response(new ModifyResponseDecorator(exchange)).build());
    }

    static class ModifyResponseDecorator extends ServerHttpResponseDecorator {
        private final ServerWebExchange exchange;
        public ModifyResponseDecorator(ServerWebExchange exchange) {
            super(exchange.getResponse());
            this.exchange = exchange;
        }


        @Override
        public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
            ClientResponse clientResponse = this.buildModifiedResponse(body);
            Mono<byte[]> modifiedBody = clientResponse.bodyToMono(byte[].class)
                    .flatMap(originBody -> Mono.just(modifyBody(originBody)));
            return ResponseUtils.writeWith(clientResponse, this.exchange, modifiedBody, byte[].class);
        }

        private ClientResponse buildModifiedResponse(final Publisher<? extends DataBuffer> body) {
            HttpHeaders httpHeaders = new HttpHeaders();
            // add origin headers
            httpHeaders.addAll(this.getHeaders());

            // reset http status
            ClientResponse clientResponse = ResponseUtils.buildClientResponse(this.getDelegate(), body);
            HttpStatus statusCode = clientResponse.statusCode();

            // reset http headers
            this.getDelegate().getHeaders().clear();
            this.getDelegate().getHeaders().putAll(httpHeaders);
            int rowStatusCode = clientResponse.rawStatusCode();
            ExchangeStrategies strategies = clientResponse.strategies();

            return ClientResponse.create(statusCode, strategies)
                    .rawStatusCode(rowStatusCode)
                    .headers(headers -> headers.addAll(httpHeaders))
                    .cookies(cookies -> cookies.addAll(this.getCookies()))
                    .body(Flux.from(body)).build();
        }

        /**
         * 修改body
         * @param responseBody
         * @return
         */
        private byte[] modifyBody(final byte[] responseBody) {
            try {
                String bodyStr = modifyBody(new String(responseBody, StandardCharsets.UTF_8));
                log.info("the body string {}", bodyStr);
                return bodyStr.getBytes(StandardCharsets.UTF_8);
            } catch (Exception e) {
                log.error("modify response error", e);
                throw new GatewayException(String.format("response modify failure. %s", e.getLocalizedMessage()));
            }
        }

        private String modifyBody(final String jsonValue) {
            JsonNode jsonNode = JsonUtils.byteToReadTree(jsonValue);
            // 操作body
            return jsonNode.asText();
        }
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
