package com.ahcloud.gateway.server.infrastructure.security.authorization.manager;

import com.ahcloud.gateway.client.enums.AppPlatformEnum;
import com.ahcloud.gateway.server.application.constant.GatewayConstants;
import com.ahcloud.gateway.server.infrastructure.security.authorization.manager.access.AccessProvider;
import com.ahcloud.gateway.server.infrastructure.security.authorization.manager.access.AccessProviderStrategyFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * @program: ahcloud-gateway
 * @description: 鉴权管理器
 * @author: YuKai Fan
 * @create: 2022/12/12 11:37
 **/
public class AuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext> {

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> authentication, AuthorizationContext context) {
        Map<String, Object> variables = context.getVariables();
        AppPlatformEnum platformEnum = (AppPlatformEnum) variables.get(GatewayConstants.APP_PLATFORM);
        AccessProvider accessProvider = AccessProviderStrategyFactory.getStrategy(platformEnum);
        return accessProvider.check(authentication, context);
    }

    @Override
    public Mono<Void> verify(Mono<Authentication> authentication, AuthorizationContext context) {
        return check(authentication, context)
                .filter(AuthorizationDecision::isGranted)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new AccessDeniedException("Access Denied"))))
                .flatMap(d -> Mono.empty());
    }
}









