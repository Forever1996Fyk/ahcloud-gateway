package com.ahcloud.gateway.server.infrastructure.security;

import cn.hutool.core.util.ArrayUtil;
import com.ahcloud.gateway.server.infrastructure.config.properties.GatewayAuthProperties;
import com.ahcloud.gateway.server.infrastructure.security.authentication.converter.AppGatewayServerAuthenticationConverter;
import com.ahcloud.gateway.server.infrastructure.security.authentication.converter.DelegatingGatewayServerAuthenticationConverter;
import com.ahcloud.gateway.server.infrastructure.security.authentication.converter.SystemWebGatewayServerAuthenticationConverter;
import com.ahcloud.gateway.server.infrastructure.security.authentication.resolver.DelegatingGatewayReactiveAuthenticationManagerResolver;
import com.ahcloud.gateway.server.infrastructure.security.authentication.resolver.GatewayReactiveAuthenticationManagerResolver;
import com.ahcloud.gateway.server.infrastructure.security.authorization.manager.AuthorizationManager;
import com.ahcloud.gateway.server.infrastructure.security.authorization.matcher.AppServerWebExchangeMatcher;
import com.ahcloud.gateway.server.infrastructure.security.authorization.matcher.SystemWebServerWebExchangeMatcher;
import com.ahcloud.gateway.server.infrastructure.security.handler.GatewayServerAccessDeniedHandler;
import com.ahcloud.gateway.server.infrastructure.security.handler.GatewayServerAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManagerResolver;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.web.server.ServerWebExchange;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

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
    private GatewayAuthProperties gatewayAuthProperties;

    @Resource
    private List<GatewayReactiveAuthenticationManagerResolver> authenticationManagerList;

    private final ServerWebExchangeMatcher[] matchers;

    public OAuth2ResourceServerConfiguration() {
        ServerAuthenticationConverter converter = createServerAuthenticationConverter();
        matchers = new ServerWebExchangeMatcher[] {
                new AppServerWebExchangeMatcher(converter),
                new SystemWebServerWebExchangeMatcher(converter)
        };
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        String[] ignoreAuthUrlArray = gatewayAuthProperties.getIgnoreAuthUrlArray();
        GatewayServerAccessDeniedHandler accessDeniedHandler = new GatewayServerAccessDeniedHandler();
        GatewayServerAuthenticationEntryPoint authenticationEntryPoint = new GatewayServerAuthenticationEntryPoint();
        http.oauth2ResourceServer()// OAuth2 资源认证处理
                    .bearerTokenConverter(createServerAuthenticationConverter())
                    .authenticationManagerResolver(createReactiveAuthenticationManagerResolver())
                    .authenticationEntryPoint(authenticationEntryPoint)
                .and()
                .authorizeExchange() // OAuth2 资源授权处理
                    // 放行 uri集合
                    .pathMatchers(ignoreAuthUrlArray).permitAll()
                    .matchers(matchers)
                    .access(new AuthorizationManager())
                .and()
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler)
                .and().csrf().disable();
        http.csrf().disable();
        return http.build();
    }

    private ServerAuthenticationConverter createServerAuthenticationConverter() {
        DelegatingGatewayServerAuthenticationConverter.Builder builder = DelegatingGatewayServerAuthenticationConverter.builder();
        SystemWebGatewayServerAuthenticationConverter systemWeb = new SystemWebGatewayServerAuthenticationConverter();
        AppGatewayServerAuthenticationConverter app = new AppGatewayServerAuthenticationConverter();
        builder.add(systemWeb);
        builder.add(app);
        return builder.build();
    }

    private ReactiveAuthenticationManagerResolver<ServerWebExchange> createReactiveAuthenticationManagerResolver() {
        return new DelegatingGatewayReactiveAuthenticationManagerResolver(authenticationManagerList);
    }
}
