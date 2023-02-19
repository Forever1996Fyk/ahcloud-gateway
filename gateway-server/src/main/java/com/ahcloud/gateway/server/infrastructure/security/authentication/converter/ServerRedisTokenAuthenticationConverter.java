package com.ahcloud.gateway.server.infrastructure.security.authentication.converter;

import com.ahcloud.common.utils.CollectionUtils;
import com.ahcloud.gateway.client.enums.AppPlatformEnum;
import com.ahcloud.gateway.client.enums.GatewayRetCodeEnum;
import com.ahcloud.gateway.server.application.constant.GatewayConstants;
import com.ahcloud.gateway.server.infrastructure.config.properties.GatewayAuthProperties;
import com.ahcloud.gateway.server.infrastructure.exception.GatewayAuthenticationException;
import com.ahcloud.gateway.server.infrastructure.security.token.RedisTokenAuthenticationToken;
import com.ahcloud.gateway.server.infrastructure.util.ServerWebExchangeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.server.PathContainer;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.pattern.PathPatternParser;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.Set;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/2/17 17:59
 **/
public class ServerRedisTokenAuthenticationConverter implements ServerAuthenticationConverter {
    private GatewayAuthProperties properties;

    private static final PathPatternParser DEFAULT_PATTERN_PARSER = new PathPatternParser();

    public ServerRedisTokenAuthenticationConverter(GatewayAuthProperties properties) {
        this.properties = properties;
    }

    public ServerRedisTokenAuthenticationConverter() {
    }

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        PathContainer path = exchange.getRequest().getPath().pathWithinApplication();
        if (properties != null) {
            Set<String> ignoreAuthUrlSet = properties.getIgnoreAuthUrlSet();
            if (CollectionUtils.isNotEmpty(ignoreAuthUrlSet)) {
                // 表示当前请求路径是否可忽略
                boolean result = ignoreAuthUrlSet.stream()
                        .map(DEFAULT_PATTERN_PARSER::parse)
                        .anyMatch(pathPattern -> pathPattern.matches(path));
                if (result) {
                    return Mono.empty();
                }
            }
        }
        String token = ServerWebExchangeUtils.getTokenFromRequest(exchange, GatewayConstants.TOKEN_PREFIX);
        if (StringUtils.isBlank(token)) {
            throw new GatewayAuthenticationException(GatewayRetCodeEnum.CERTIFICATE_EXCEPTION_ERROR);
        }
        AppPlatformEnum platform = ServerWebExchangeUtils.getAppPlatformByRequest(exchange);
        if (Objects.isNull(platform)) {
            throw new GatewayAuthenticationException(GatewayRetCodeEnum.SYSTEM_ERROR);
        }
        exchange.getAttributes().put(GatewayConstants.APP_PLATFORM, platform);
        return Mono.just(new RedisTokenAuthenticationToken(token, platform.getValue(), platform));
    }
}
