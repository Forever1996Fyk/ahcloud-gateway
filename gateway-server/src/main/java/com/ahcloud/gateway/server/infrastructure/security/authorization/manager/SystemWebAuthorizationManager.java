package com.ahcloud.gateway.server.infrastructure.security.authorization.manager;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import reactor.core.publisher.Mono;

/**
 * @program: ahcloud-gateway
 * @description: 鉴权管理器
 * @author: YuKai Fan
 * @create: 2022/12/12 11:37
 **/
public class SystemWebAuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext> {

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
    public Mono<Void> verify(Mono<Authentication> authentication, AuthorizationContext context) {
        return check(authentication, context)
                .filter(AuthorizationDecision::isGranted)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new AccessDeniedException("Access Denied"))))
                .flatMap(d -> Mono.empty());
    }
}









