package com.ahcloud.gateway.server.infrastructure.security.authentication;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.ReactiveAuthenticationManagerResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @program: ahcloud-gateway
 * @description: 根据ServerWebExchange, 获取特定的ReactiveAuthenticationManager
 * @author: YuKai Fan
 * @create: 2023/1/17 22:57
 **/
public class AbstractReactiveAuthenticationManagerResolver implements ReactiveAuthenticationManagerResolver<ServerWebExchange> {

    public AbstractReactiveAuthenticationManagerResolver() {
    }

    @Override
    public Mono<ReactiveAuthenticationManager> resolve(ServerWebExchange context) {
        return null;
    }
}
