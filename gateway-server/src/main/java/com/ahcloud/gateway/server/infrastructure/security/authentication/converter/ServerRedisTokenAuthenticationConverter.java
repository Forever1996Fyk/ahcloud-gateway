package com.ahcloud.gateway.server.infrastructure.security.authentication.converter;

import com.ahcloud.gateway.client.enums.AppPlatformEnum;
import com.ahcloud.gateway.client.enums.GatewayRetCodeEnum;
import com.ahcloud.gateway.core.domain.dto.ApiGatewayDTO;
import com.ahcloud.gateway.core.domain.context.GatewayContext;
import com.ahcloud.gateway.core.infrastructure.constant.GatewayConstants;
import com.ahcloud.gateway.client.exception.GatewayException;
import com.ahcloud.gateway.core.infrastructure.util.ServerWebExchangeUtils;
import com.ahcloud.gateway.server.infrastructure.exception.GatewayAuthenticationException;
import com.ahcloud.gateway.server.infrastructure.security.token.RedisTokenAuthenticationToken;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/2/17 17:59
 **/
public class ServerRedisTokenAuthenticationConverter implements ServerAuthenticationConverter {

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        Object o = exchange.getAttributes().get(GatewayContext.CACHE_GATEWAY_CONTEXT);
        if (Objects.isNull(o)) {
            throw new GatewayException(GatewayRetCodeEnum.SYSTEM_ERROR);
        }
        GatewayContext gatewayContext = (GatewayContext) o;
        ApiGatewayDTO apiGatewayDTO = gatewayContext.getApiGatewayDTO();
        // 如果接口信息为空，默认匿名访问
        if (Objects.isNull(apiGatewayDTO)) {
            return Mono.empty();
        }
        // 当前接口无需认证
        if (!apiGatewayDTO.getAuth()) {
            return Mono.empty();
        }
        String token = ServerWebExchangeUtils.getTokenFromRequest(exchange, GatewayConstants.TOKEN_PREFIX);
        if (StringUtils.isBlank(token)) {
            throw new GatewayAuthenticationException(GatewayRetCodeEnum.CERTIFICATE_EXCEPTION_ERROR);
        }
        AppPlatformEnum platform = gatewayContext.getAppPlatformEnum();
        return Mono.just(new RedisTokenAuthenticationToken(token, platform.getValue(), platform));
    }
}
