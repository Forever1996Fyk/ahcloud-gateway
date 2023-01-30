package com.ahcloud.gateway.server.infrastructure.security.authorization.manager.access;

import com.ahcloud.gateway.client.AppPlatformEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/1/18 20:04
 **/
@Slf4j
@Component
public class SystemWebAccessProvider implements AccessProvider {

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> authentication, AuthorizationContext context) {
        String uri = context.getExchange().getRequest().getURI().getPath();
        // 根据uri获取 权限标识
        String authority = "";
        return authentication
                .filter(Authentication::isAuthenticated)
                .flatMapIterable(Authentication::getAuthorities)
                .map(GrantedAuthority::getAuthority)
                .any(authority::contains)
                .map(result -> {
                    AuthorizationDecision authorizationDecision = new AuthorizationDecision(true);
                    return authorizationDecision;
                })
                .defaultIfEmpty(new AuthorizationDecision(false));
    }

    @Override
    public AppPlatformEnum getAppPlatform() {
        return AppPlatformEnum.SYSTEM_WEB;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        AccessProviderStrategyFactory.register(AppPlatformEnum.SYSTEM_WEB, this);
    }
}
