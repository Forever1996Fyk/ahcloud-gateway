package com.ahcloud.gateway.server.infrastructure.security.authorization.manager.access;

import com.ahcloud.common.utils.CollectionUtils;
import com.ahcloud.gateway.server.infrastructure.config.properties.GatewayAuthProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.PathContainer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/2/16 10:56
 **/
@Slf4j
public abstract class AbstractAccessProvider implements AccessProvider {
    private static final PathPatternParser DEFAULT_PATTERN_PARSER = new PathPatternParser();
    private final Set<String> ignoreAuthUrlSet;

    protected AbstractAccessProvider(Set<String> ignoreAuthUrlSet) {
        this.ignoreAuthUrlSet = ignoreAuthUrlSet;
    }

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> authentication, AuthorizationContext context) {
        ServerWebExchange exchange = context.getExchange();
        ServerHttpRequest request = exchange.getRequest();
        PathContainer path = request.getPath().pathWithinApplication();
        log.info("{}[check] current path is {}", getLogMark(), path.value());
        if (CollectionUtils.isNotEmpty(ignoreAuthUrlSet)) {
            // 表示当前请求路径可忽略
            boolean result = ignoreAuthUrlSet.stream()
                    .map(DEFAULT_PATTERN_PARSER::parse)
                    .anyMatch(pathPattern -> pathPattern.matches(path));
            if (result) {
                log.info("{}[check] current path ignore auth {}", getLogMark(), path.value());
                return Mono.just(new AuthorizationDecision(true));
            }
        }
        return doCheck(authentication, context);
    }

    /**
     * 执行校验
     * @param authentication 认证信息
     * @param context 请求上下文
     * @return
     */
    protected abstract Mono<AuthorizationDecision> doCheck(Mono<Authentication> authentication, AuthorizationContext context);

    /**
     * 日志标识
     * @return
     */
    protected abstract String getLogMark();
}
