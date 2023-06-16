package com.ahcloud.gateway.common.sentinel.config;

import com.ahcloud.gateway.common.sentinel.api.SentinelApiDefinitionHandler;
import com.ahcloud.gateway.common.sentinel.api.impl.SentinelApiDefinitionHandlerImpl;
import com.ahcloud.gateway.common.sentinel.callback.GatewayBlockRequestHandler;
import com.alibaba.csp.sentinel.adapter.gateway.sc.SentinelGatewayFilter;
import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.GatewayCallbackManager;
import com.alibaba.csp.sentinel.adapter.gateway.sc.exception.SentinelGatewayBlockExceptionHandler;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.reactive.result.view.ViewResolver;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/6/14 16:00
 **/
@Configuration
public class SentinelGatewayConfiguration {

    private final List<ViewResolver> viewResolvers;
    private final ServerCodecConfigurer serverCodecConfigurer;

    public SentinelGatewayConfiguration(ObjectProvider<List<ViewResolver>> viewResolversProvider, ServerCodecConfigurer serverCodecConfigurer) {
        this.viewResolvers = viewResolversProvider.getIfAvailable(Collections::emptyList);
        this.serverCodecConfigurer = serverCodecConfigurer;
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SentinelGatewayBlockExceptionHandler sentinelGatewayBlockExceptionHandler() {
        // Register the block exception handler for Spring Cloud Gateway.
        return new SentinelGatewayBlockExceptionHandler(this.viewResolvers, this.serverCodecConfigurer);
    }

    @Bean
    @Order(-1)
    public SentinelGatewayFilter sentinelGatewayFilter() {
        return new SentinelGatewayFilter();
    }

    @Bean
    public SentinelApiDefinitionHandler sentinelApiDefinitionHandler() {
        return new SentinelApiDefinitionHandlerImpl();
    }

    /**
     * 初始化 回调 异常处理
     */
    public void init() {
        GatewayCallbackManager.setBlockHandler(new GatewayBlockRequestHandler());
        // 设置 限流跳转url
//        GatewayCallbackManager.setBlockHandler(new RedirectBlockRequestHandler(""));
        // 设置 ip黑名单
        GatewayCallbackManager.setRequestOriginParser(exchange -> {
            ServerHttpRequest request = exchange.getRequest();
            return Objects.requireNonNull(request.getRemoteAddress()).toString();
        });
    }
}
