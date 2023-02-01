package com.ahcloud.gateway.server.infrastructure.security.authentication.converter;

import com.ahcloud.gateway.client.enums.AppPlatformEnum;
import com.ahcloud.gateway.client.enums.GatewayRetCodeEnum;
import com.ahcloud.gateway.server.infrastructure.exception.GatewayException;
import com.ahcloud.gateway.server.infrastructure.util.ServerWebExchangeUtils;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/1/18 09:46
 **/
@Slf4j
@AllArgsConstructor
public class DelegatingGatewayServerAuthenticationConverter implements ServerAuthenticationConverter {
    private final List<GatewayServerAuthenticationConverter> mappings;

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        AppPlatformEnum platform = ServerWebExchangeUtils.getAppPlatformByRequest(exchange);
        return Flux.fromIterable(mappings)
                .concatMap(mapping -> mapping.getAppPlatform()
                        .filter(item -> Objects.equals(item, platform))
                        .flatMap(item -> mapping.convert(exchange))
                )
                .next()
                .doOnError((e) -> {
                    log.error("DelegatingGatewayServerAuthenticationConverter[convert] platformEnum is {}, reason is {}", platform, Throwables.getStackTraceAsString(e));
                    throw new GatewayException(GatewayRetCodeEnum.SYSTEM_ERROR);
                });
    }

    public static DelegatingGatewayServerAuthenticationConverter.Builder builder() {
        return new DelegatingGatewayServerAuthenticationConverter.Builder();
    }

    public static class Builder {
        private final List<GatewayServerAuthenticationConverter> mappings = Lists.newArrayList();

        private Builder() {
        }

        public DelegatingGatewayServerAuthenticationConverter.Builder add(GatewayServerAuthenticationConverter converter) {
            this.mappings.add(converter);
            return this;
        }

        public DelegatingGatewayServerAuthenticationConverter build() {
            return new DelegatingGatewayServerAuthenticationConverter(mappings);
        }
    }
}
