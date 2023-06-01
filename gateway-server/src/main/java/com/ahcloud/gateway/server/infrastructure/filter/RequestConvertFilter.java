package com.ahcloud.gateway.server.infrastructure.filter;

import com.ahcloud.gateway.client.enums.AppPlatformEnum;
import com.ahcloud.gateway.client.enums.GatewayRetCodeEnum;
import com.ahcloud.gateway.core.domain.context.GatewayContext;
import com.ahcloud.gateway.core.infrastructure.exception.GatewayException;
import com.ahcloud.gateway.core.infrastructure.util.ServerWebExchangeUtils;
import com.ahcloud.kernel.core.common.Constant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * @program: ahcloud-gateway
 * @description: 请求过滤器
 * @author: YuKai Fan
 * @create: 2023/1/16 08:54
 **/
@Slf4j
@Component
public class RequestConvertFilter implements WebFilter, Ordered {

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        AppPlatformEnum appPlatformEnum = ServerWebExchangeUtils.getAppPlatformByRequest(exchange);
        if (Objects.isNull(appPlatformEnum)) {
            throw new GatewayException(GatewayRetCodeEnum.SYSTEM_ERROR);
        }
        HttpHeaders headers = request.getHeaders();
        String tenantId = "";
        if (appPlatformEnum.needTenant()) {
            tenantId = headers.getFirst(Constant.CTX_KEY_TENANT_ID.toString());
            if (StringUtils.isBlank(tenantId)) {
                throw new GatewayException(GatewayRetCodeEnum.GATEWAY_PARAM_MISS);
            }
        }
        GatewayContext context = GatewayContext.builder()
                .pathContainer(request.getPath().pathWithinApplication())
                .ipAddress(String.valueOf(request.getRemoteAddress()))
                .origin(request.getHeaders().getOrigin())
                .headers(headers)
                .appPlatformEnum(appPlatformEnum)
                .tenantId(tenantId)
                .build();
        //注意，因为webflux的响应式编程 不能再采取原先的编码方式 即应该先将gatewayContext放入exchange中，否则其他地方可能取不到
        exchange.getAttributes().put(GatewayContext.CACHE_GATEWAY_CONTEXT, context);
        return chain.filter(exchange);
    }
}
