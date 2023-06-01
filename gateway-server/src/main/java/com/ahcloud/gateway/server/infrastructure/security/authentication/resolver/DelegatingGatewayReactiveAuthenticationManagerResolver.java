package com.ahcloud.gateway.server.infrastructure.security.authentication.resolver;

import com.ahcloud.gateway.client.enums.AppPlatformEnum;
import com.ahcloud.gateway.client.enums.GatewayRetCodeEnum;
import com.ahcloud.gateway.core.infrastructure.exception.GatewayException;
import com.ahcloud.gateway.core.infrastructure.util.ServerWebExchangeUtils;
import com.google.common.base.Throwables;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.ReactiveAuthenticationManagerResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/1/18 16:16
 **/
@Slf4j
@AllArgsConstructor
public class DelegatingGatewayReactiveAuthenticationManagerResolver implements ReactiveAuthenticationManagerResolver<ServerWebExchange> {

    private final List<GatewayReactiveAuthenticationManagerResolver> endpointList;

    @Override
    public Mono<ReactiveAuthenticationManager> resolve(ServerWebExchange exchange) {
        AppPlatformEnum platformEnum = ServerWebExchangeUtils.getAppPlatformByRequest(exchange);
        return Flux.fromIterable(endpointList)
                .concatMap(
                        endpoint -> endpoint.getAppPlatform()
                                .filter(item -> Objects.equals(item, platformEnum))
                                .flatMap(item -> endpoint.resolve(exchange))
                )
                .next()
                .doOnError((e) -> {
                    log.error("DelegatingGatewayReactiveAuthenticationManagerResolver[resolve] platformEnum is {}, reason is {}", platformEnum, Throwables.getStackTraceAsString(e));
                    throw new GatewayException(GatewayRetCodeEnum.SYSTEM_ERROR);
                });
    }
}
