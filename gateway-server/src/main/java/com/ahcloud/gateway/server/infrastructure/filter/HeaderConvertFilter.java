package com.ahcloud.gateway.server.infrastructure.filter;

import com.ahcloud.common.utils.NullUtils;
import com.ahcloud.gateway.core.domain.dto.UserInfoDTO;
import com.ahcloud.gateway.core.domain.context.GatewayContext;
import com.ahcloud.kernel.core.common.Constant;
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
            @Override
            public HttpHeaders getHeaders() {
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.putAll(super.getHeaders());

                if (Objects.nonNull(userInfoDTO)) {
                    httpHeaders.set(Constant.CTX_KEY_USER_ID.toString(), NullUtils.of(userInfoDTO.getUserId()));
                    httpHeaders.set(Constant.CTX_KEY_USER_NAME.toString(), NullUtils.of(userInfoDTO.getUserName()));
                    httpHeaders.set(Constant.CTX_KEY_USER_ID.toString(), NullUtils.of(userInfoDTO.getUserId()));
                    httpHeaders.set(Constant.CTX_KEY_TOKEN.toString(), NullUtils.of(userInfoDTO.getToken()));
                    Long tenantId = userInfoDTO.getTenantId();
                    if (tenantId != null) {
                        httpHeaders.set(Constant.CTX_KEY_TENANT_ID.toString(), String.valueOf(NullUtils.of(tenantId)));
                    }
                }
                httpHeaders.set(Constant.CTX_KEY_ORIGIN.toString(), NullUtils.of(gatewayContext.getOrigin()));
                httpHeaders.set(Constant.CTX_KEY_CLIENT_IP.toString(), NullUtils.of(gatewayContext.getIpAddress()));
                httpHeaders.set(Constant.CTX_KEY_GW_APP_PLATFORM.toString(), NullUtils.of(gatewayContext.getAppPlatformEnum().getValue()));
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
