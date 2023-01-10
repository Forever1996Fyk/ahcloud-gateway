package com.ahcloud.gateway.server.infrastructure.security;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * @program: ahcloud-gateway
 * @description: 鉴权管理器
 * @author: YuKai Fan
 * @create: 2022/12/12 11:37
 **/
@Component
public class AuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext> {

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> authentication, AuthorizationContext context) {
        String uri = context.getExchange().getRequest().getURI().getPath();
        /*
         1. 判断当前路径是否无需认证，即放行
         2. 根据当前路径获取对应的权限标识
            2.1 如果获取为空, 返回false
         3. 根据获取到的权限标识，匹配当前用户认证信息中的权限集合
         */
        return Mono.just(new AuthorizationDecision(true));
//        String authority = "";
//        return authentication
//                .filter(Authentication::isAuthenticated)
//                .flatMapIterable(Authentication::getAuthorities)
//                .map(GrantedAuthority::getAuthority)
//                .any(authority::contains)
//                .map(result -> {
//                    AuthorizationDecision authorizationDecision = new AuthorizationDecision(true);
//                    // todo
////                    context.getExchange().getRequest().mutate().header();
//                    return authorizationDecision;
//                })
//                .defaultIfEmpty(new AuthorizationDecision(false));
    }

    @Override
    public Mono<Void> verify(Mono<Authentication> authentication, AuthorizationContext object) {
        return check(authentication, object)
                .filter(AuthorizationDecision::isGranted)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new AccessDeniedException("Access Denied"))))
                .flatMap(d -> Mono.empty());
    }
}









