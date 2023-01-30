package com.ahcloud.gateway.server.infrastructure.security.authentication.converter;

import com.ahcloud.gateway.client.AppPlatformEnum;
import com.ahcloud.gateway.server.application.constant.GatewayConstants;
import com.ahcloud.gateway.server.infrastructure.security.token.RedisTokenAuthenticationToken;
import com.ahcloud.gateway.server.infrastructure.util.ServerWebExchangeUtils;
import org.springframework.security.core.Authentication;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/1/18 10:15
 **/
public class SystemWebGatewayServerAuthenticationConverter implements GatewayServerAuthenticationConverter {
    @Override
    public Mono<AppPlatformEnum> getAppPlatform() {
        return Mono.just(AppPlatformEnum.SYSTEM_WEB);
    }

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        String token = ServerWebExchangeUtils.getTokenFromRequest(exchange, GatewayConstants.TOKEN_HEADER);
        return Mono.just(new RedisTokenAuthenticationToken(token,  AppPlatformEnum.SYSTEM_WEB.getValue()));
    }
}
