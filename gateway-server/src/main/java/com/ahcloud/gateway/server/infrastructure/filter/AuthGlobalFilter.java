package com.ahcloud.gateway.server.infrastructure.filter;

import com.ahcloud.gateway.server.application.constant.GatewayConstants;
import com.ahcloud.kernel.core.common.Constant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.skywalking.apm.toolkit.trace.TraceContext;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @program: ahcloud-gateway
 * @description: 授权资源过滤器
 * @author: YuKai Fan
 * @create: 2022/12/12 11:21
 **/
@Slf4j
@Component
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        String token = exchange.getRequest().getHeaders().getFirst(GatewayConstants.TOKEN_HEADER);
//        if (StringUtils.isBlank(token)) {
//            return chain.filter(exchange);
//        }
//        String readToken = parsingToken(token);
//        if (StringUtils.isBlank(readToken)) {
//            return chain.filter(exchange);
//        }
        try {
            // 将当前用户id放入请求头，用于传递到其他filter以及下游服务
//            exchange.getRequest().mutate().header(Constant.CTX_KEY_USER_ID.toString(), "10000000");
            // 放入traceId
//            exchange.getRequest().mutate().header(Constant.CTX_KEY_TRACE_ID.toString(), TraceContext.traceId());
        } catch (Exception e) {
            e.printStackTrace();
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
