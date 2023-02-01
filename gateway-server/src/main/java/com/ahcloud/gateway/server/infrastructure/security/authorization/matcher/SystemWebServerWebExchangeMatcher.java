package com.ahcloud.gateway.server.infrastructure.security.authorization.matcher;

import com.ahcloud.gateway.client.enums.AppPlatformEnum;
import com.ahcloud.gateway.server.application.constant.GatewayConstants;
import com.ahcloud.gateway.server.infrastructure.security.authentication.converter.SystemWebGatewayServerAuthenticationConverter;
import com.google.common.collect.Maps;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher.MatchResult.match;
import static org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher.MatchResult.notMatch;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/1/18 16:36
 **/
@Setter
public class SystemWebServerWebExchangeMatcher implements ServerWebExchangeMatcher {

    private ServerAuthenticationConverter converter = new SystemWebGatewayServerAuthenticationConverter();

    @Override
    public Mono<MatchResult> matches(ServerWebExchange exchange) {
        return converter.convert(exchange)
                .flatMap(this::nullAuthentication)
                .onErrorResume(e -> notMatch());
    }

    private Mono<MatchResult> nullAuthentication(Authentication authentication) {
        Map<String, Object> variables = Maps.newHashMap();
        variables.put(GatewayConstants.APP_PLATFORM, AppPlatformEnum.SYSTEM_WEB);
        return authentication == null ? notMatch() : match(variables);
    }
}
