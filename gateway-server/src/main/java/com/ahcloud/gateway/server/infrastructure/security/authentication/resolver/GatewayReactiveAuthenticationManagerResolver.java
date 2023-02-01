package com.ahcloud.gateway.server.infrastructure.security.authentication.resolver;

import com.ahcloud.gateway.client.enums.AppPlatformEnum;
import org.springframework.security.authentication.ReactiveAuthenticationManagerResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/1/18 10:36
 **/
public interface GatewayReactiveAuthenticationManagerResolver extends ReactiveAuthenticationManagerResolver<ServerWebExchange> {

    /**
     * app平台类型
     *
     * @return AppPlatformEnum
     */
    Mono<AppPlatformEnum> getAppPlatform();
}
