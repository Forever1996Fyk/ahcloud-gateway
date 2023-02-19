package com.ahcloud.gateway.server.infrastructure.security.authorization.matcher;

import com.ahcloud.gateway.client.enums.AppPlatformEnum;
import com.ahcloud.gateway.server.application.constant.GatewayConstants;
import com.ahcloud.gateway.server.infrastructure.security.token.RedisTokenAuthenticationToken;
import com.ahcloud.gateway.server.infrastructure.util.ServerWebExchangeUtils;
import com.google.common.collect.Maps;
import lombok.Setter;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Objects;

import static org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher.MatchResult.match;
import static org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher.MatchResult.notMatch;

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
        AppPlatformEnum platformEnum = (AppPlatformEnum) exchange.getAttributes().get(GatewayConstants.APP_PLATFORM);
        if (Objects.isNull(platformEnum)) {
            return notMatch();
        }
        // 此处数据会在 DelegatingReactiveAuthorizationManager 鉴权管理中心的check方法中会用到
        Map<String, Object> variables = Maps.newHashMap();
        variables.put(GatewayConstants.APP_PLATFORM, platformEnum);
        return match(variables);
    }
}
