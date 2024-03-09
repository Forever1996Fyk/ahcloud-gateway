package com.ahcloud.gateway.server.infrastructure.filter;

import com.ahcloud.common.constant.Constant;
import com.ahcloud.common.utils.NullUtils;
import com.ahcloud.gateway.core.domain.dto.UserInfoDTO;
import com.ahcloud.gateway.core.domain.context.GatewayContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/2/21 09:54
 **/
@Slf4j
@Component
public class HeaderConvertFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        HttpHeaders headers = request.getHeaders();
        log.info("[HeaderConvertFilter] HttpMethod is {}, Url is {}", request.getMethod(), request.getURI().getRawPath());
        Object o = exchange.getAttributes().get(GatewayContext.CACHE_GATEWAY_CONTEXT);
        if (Objects.isNull(o)) {
            return chain.filter(exchange);
        }
        GatewayContext gatewayContext = (GatewayContext) o;
        UserInfoDTO userInfoDTO = gatewayContext.getUserInfoDTO();
        // 处理参数
        MediaType contentType = headers.getContentType();
        ServerHttpRequest mutatedRequest = new ServerHttpRequestDecorator(exchange.getRequest()) {
            /**
             * 这里重写请求头，目的是传入下游服务参数，例如：用户信息，请求来源，客户端ip等等。也可以自定义
             * @return
             */
            @Override
            public HttpHeaders getHeaders() {
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.putAll(super.getHeaders());

                if (Objects.nonNull(userInfoDTO)) {
                    httpHeaders.set(Constant.CTX_KEY_USER_ID, NullUtils.of(userInfoDTO.getUserId()));
                    httpHeaders.set(Constant.CTX_KEY_USER_NAME, NullUtils.of(userInfoDTO.getUserName()));
                    httpHeaders.set(Constant.CTX_KEY_TOKEN, NullUtils.of(userInfoDTO.getToken()));
                    // 有租户的话可以添加租户id
//                    Long tenantId = userInfoDTO.getTenantId();
//                    if (tenantId != null) {
//                        httpHeaders.set(Constant.CTX_KEY_TENANT_ID.toString(), String.valueOf(NullUtils.of(tenantId)));
//                    }
                }
                httpHeaders.set(Constant.CTX_KEY_ORIGIN, NullUtils.of(gatewayContext.getOrigin()));
                httpHeaders.set(Constant.CTX_KEY_CLIENT_IP, NullUtils.of(gatewayContext.getIpAddress()));
                httpHeaders.set(Constant.CTX_KEY_GW_APP_PLATFORM, NullUtils.of(gatewayContext.getAppPlatformEnum().getValue()));
                return httpHeaders;
            }
        };
        log.info("[HeaderConvertFilter] GatewayContext contentType is {}, Gateway context is set with {}", contentType, gatewayContext);
        return chain.filter(exchange.mutate().request(mutatedRequest).build());
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
