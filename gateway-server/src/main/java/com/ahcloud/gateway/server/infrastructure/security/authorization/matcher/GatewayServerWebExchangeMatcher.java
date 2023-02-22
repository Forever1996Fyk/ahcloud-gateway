package com.ahcloud.gateway.server.infrastructure.security.authorization.matcher;

import com.ahcloud.gateway.client.enums.AppPlatformEnum;
import com.ahcloud.gateway.core.domain.context.GatewayContext;
import com.ahcloud.gateway.core.infrastructure.constant.GatewayConstants;
import com.google.common.collect.Maps;
import lombok.Setter;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher.MatchResult.match;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/1/18 16:36
 **/
@Setter
public class GatewayServerWebExchangeMatcher implements ServerWebExchangeMatcher {

    @Override
    public Mono<MatchResult> matches(ServerWebExchange exchange) {
        return nullPlatform(exchange);
    }

    private Mono<MatchResult> nullPlatform(ServerWebExchange exchange) {
        GatewayContext gatewayContext = (GatewayContext) exchange.getAttributes().get(GatewayContext.CACHE_GATEWAY_CONTEXT);
        AppPlatformEnum platformEnum = gatewayContext.getAppPlatformEnum();
        // 此处数据会在 DelegatingReactiveAuthorizationManager 鉴权管理中心的check方法中会用到
        Map<String, Object> variables = Maps.newHashMap();
        variables.put(GatewayConstants.APP_PLATFORM, platformEnum);
        return match(variables);
    }
}
