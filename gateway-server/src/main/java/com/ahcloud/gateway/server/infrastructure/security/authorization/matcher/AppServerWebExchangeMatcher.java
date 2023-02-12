package com.ahcloud.gateway.server.infrastructure.security.authorization.matcher;

import com.ahcloud.gateway.client.enums.AppPlatformEnum;
import com.ahcloud.gateway.server.application.constant.GatewayConstants;
import com.ahcloud.gateway.server.infrastructure.security.authentication.converter.AppGatewayServerAuthenticationConverter;
import com.ahcloud.gateway.server.infrastructure.security.token.RedisTokenAuthenticationToken;
import com.google.common.collect.Maps;
import lombok.Setter;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
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
public class AppServerWebExchangeMatcher implements ServerWebExchangeMatcher {

    private final ServerAuthenticationConverter converter;

    public AppServerWebExchangeMatcher(ServerAuthenticationConverter converter) {
        this.converter = converter;
    }


    @Override
    public Mono<MatchResult> matches(ServerWebExchange exchange) {
        return converter.convert(exchange)
                .cast(RedisTokenAuthenticationToken.class)
                .flatMap(this::nullAuthentication)
                .onErrorResume(e -> notMatch());
    }

    private Mono<MatchResult> nullAuthentication(RedisTokenAuthenticationToken authentication) {
        if (Objects.isNull(authentication) || (AppPlatformEnum.APP != authentication.getAppPlatformEnum())) {
            return notMatch();
        }
        Map<String, Object> variables = Maps.newHashMap();
        variables.put(GatewayConstants.APP_PLATFORM, authentication.getAppPlatformEnum());
        return match(variables);
    }
}
