package com.ahcloud.gateway.server.infrastructure.security.authentication.resolver;

import com.ahcloud.gateway.client.enums.AppPlatformEnum;
import com.ahcloud.gateway.server.infrastructure.security.authentication.manager.AppReactiveAuthenticationManager;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/1/18 10:50
 **/
@Component("appGatewayReactiveAuthenticationManagerResolver")
public class AppGatewayReactiveAuthenticationManagerResolver implements GatewayReactiveAuthenticationManagerResolver {
    @Resource
    private AppReactiveAuthenticationManager authenticationManager;

    @Override
    public Mono<AppPlatformEnum> getAppPlatform() {
        return Mono.just(AppPlatformEnum.APP);
    }

    @Override
    public Mono<ReactiveAuthenticationManager> resolve(ServerWebExchange exchange) {
        return Mono.just(authenticationManager);
    }
}
