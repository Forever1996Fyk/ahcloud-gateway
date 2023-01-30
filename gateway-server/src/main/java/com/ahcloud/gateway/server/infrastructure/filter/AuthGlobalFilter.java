package com.ahcloud.gateway.server.infrastructure.filter;

import com.ahcloud.common.utils.CollectionUtils;
import com.ahcloud.gateway.server.application.constant.GatewayConstants;
import com.ahcloud.gateway.server.infrastructure.config.GatewayAuthProperties;
import com.ahcloud.gateway.server.infrastructure.util.PathMatchUtils;
import com.ahcloud.kernel.core.common.Constant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.skywalking.apm.toolkit.trace.TraceContext;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.Set;

/**
 * @program: ahcloud-gateway
 * @description: 授权资源过滤器
 * @author: YuKai Fan
 * @create: 2022/12/12 11:21
 **/
@Slf4j
@Component
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    @Resource
    private GatewayAuthProperties gatewayAuthProperties;

    /**
     * 认证权限过滤
     *
     * 1、 获取当前请求地址
     * 2、判断当前地址是否需要认证权限
     *    2.1、如果需要认证
     *         2.1.1、获取请求头的token, 如果为空则返回权限错误, 否则直接放行(接口权限的校验交给 SystemWebAuthorizationManager)
     *    2.2、不需要认证
     *          2.2.1、直接放行
     * @param exchange
     * @param chain
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        Set<String> ignoreTokenUrlSet = gatewayAuthProperties.getIgnoreTokenUrlSet();
        if (CollectionUtils.isNotEmpty(ignoreTokenUrlSet)) {
            // 当前请求路径 存在与忽略token的集合中, 则直接放行
            if (PathMatchUtils.pathAnyMatch(path, ignoreTokenUrlSet)) {
                return chain.filter(exchange);
            }
        }
        String token = request.getHeaders().getFirst(GatewayConstants.TOKEN_HEADER);
        if (StringUtils.isBlank(token)) {
            return chain.filter(exchange);
        }
        String readToken = parsingToken(token);
        if (StringUtils.isBlank(readToken)) {
            return chain.filter(exchange);
        }
        return chain.filter(exchange);
    }

    private String parsingToken(String oldToken) {
        // 如果以Bearer开头，则提取。
        if (StringUtils.startsWith(oldToken, GatewayConstants.TOKEN_PREFIX.toLowerCase())) {
            String authHeaderValue = StringUtils.substring(oldToken, GatewayConstants.TOKEN_PREFIX.length()).trim();
            int commaIndex = authHeaderValue.indexOf(',');
            if (commaIndex > 0) {
                authHeaderValue = authHeaderValue.substring(0, commaIndex);
            }
            return authHeaderValue;
        }
        return null;
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
