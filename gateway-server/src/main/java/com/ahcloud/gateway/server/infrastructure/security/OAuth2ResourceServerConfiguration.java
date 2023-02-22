package com.ahcloud.gateway.server.infrastructure.security;

import com.ahcloud.gateway.server.infrastructure.security.authentication.converter.ServerRedisTokenAuthenticationConverter;
import com.ahcloud.gateway.server.infrastructure.security.authentication.resolver.DelegatingGatewayReactiveAuthenticationManagerResolver;
import com.ahcloud.gateway.server.infrastructure.security.authentication.resolver.GatewayReactiveAuthenticationManagerResolver;
import com.ahcloud.gateway.server.infrastructure.security.authorization.manager.AuthorizationManager;
import com.ahcloud.gateway.server.infrastructure.security.authorization.matcher.GatewayServerWebExchangeMatcher;
import com.ahcloud.gateway.server.infrastructure.security.handler.GatewayServerAccessDeniedHandler;
import com.ahcloud.gateway.server.infrastructure.security.handler.GatewayServerAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManagerResolver;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.web.server.ServerWebExchange;

import javax.annotation.Resource;
import java.util.List;

/**
 * @program: ahcloud-gateway
 * @description: 资源服务器
 * @author: YuKai Fan
 * @create: 2022/12/12 11:23
 **/
@Configuration
@EnableWebFluxSecurity
public class OAuth2ResourceServerConfiguration {

    @Resource
    private List<GatewayReactiveAuthenticationManagerResolver> authenticationManagerList;


    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        GatewayServerAccessDeniedHandler accessDeniedHandler = new GatewayServerAccessDeniedHandler();
        GatewayServerAuthenticationEntryPoint authenticationEntryPoint = new GatewayServerAuthenticationEntryPoint();
        ServerWebExchangeMatcher[] matchers = new ServerWebExchangeMatcher[] {
                new GatewayServerWebExchangeMatcher(),
        };
        http.anonymous()
                .and()
                .oauth2ResourceServer()// OAuth2 资源认证处理
                    .bearerTokenConverter(new ServerRedisTokenAuthenticationConverter())
                    .authenticationManagerResolver(createReactiveAuthenticationManagerResolver())
                    .authenticationEntryPoint(authenticationEntryPoint)
                .and()
                .authorizeExchange() // OAuth2 资源授权处理
                    // 放行 uri集合
                    .matchers(matchers)
                    .access(new AuthorizationManager())
                .and()
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler)
                .and().csrf().disable();
        return http.build();
    }

    private ReactiveAuthenticationManagerResolver<ServerWebExchange> createReactiveAuthenticationManagerResolver() {
        return new DelegatingGatewayReactiveAuthenticationManagerResolver(authenticationManagerList);
    }
}
