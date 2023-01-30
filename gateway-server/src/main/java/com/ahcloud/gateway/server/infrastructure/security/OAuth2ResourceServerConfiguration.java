package com.ahcloud.gateway.server.infrastructure.security;

import cn.hutool.core.util.ArrayUtil;
import com.ahcloud.gateway.server.infrastructure.security.authentication.converter.DelegatingGatewayServerAuthenticationConverter;
import com.ahcloud.gateway.server.infrastructure.security.authentication.converter.SystemWebGatewayServerAuthenticationConverter;
import com.ahcloud.gateway.server.infrastructure.security.authentication.resolver.DelegatingGatewayReactiveAuthenticationManagerResolver;
import com.ahcloud.gateway.server.infrastructure.security.authentication.resolver.GatewayReactiveAuthenticationManagerResolver;
import com.ahcloud.gateway.server.infrastructure.security.authorization.manager.AppAuthorizationManager;
import com.ahcloud.gateway.server.infrastructure.security.authorization.manager.AuthorizationManager;
import com.ahcloud.gateway.server.infrastructure.security.authorization.manager.SystemWebAuthorizationManager;
import com.ahcloud.gateway.server.infrastructure.security.authorization.matcher.AppServerWebExchangeMatcher;
import com.ahcloud.gateway.server.infrastructure.security.authorization.matcher.SystemWebServerWebExchangeMatcher;
import com.google.common.collect.Lists;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.DelegatingReactiveAuthenticationManager;
import org.springframework.security.authentication.ReactiveAuthenticationManagerResolver;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.security.web.server.authorization.DelegatingReactiveAuthorizationManager;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcherEntry;
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

    private final ServerWebExchangeMatcher[] matchers = new ServerWebExchangeMatcher[] {
            new AppServerWebExchangeMatcher(),
            new SystemWebServerWebExchangeMatcher()
    };

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        List<String> ignoreUriList = Lists.newArrayList();
        http.authorizeExchange()
                // 放行 uri集合
                .pathMatchers(ArrayUtil.toArray(ignoreUriList, String.class)).permitAll()
                .and()
                .oauth2ResourceServer()// OAuth2 资源认证处理
                    .bearerTokenConverter(createServerAuthenticationConverter())
                    .authenticationManagerResolver(createReactiveAuthenticationManagerResolver())
                .and()
                .authorizeExchange() // OAuth2 资源授权处理
                    .matchers(matchers)
                    .access(new AuthorizationManager())
                .and()
                .exceptionHandling().accessDeniedHandler((exchange, denied) -> null)
                .and().csrf().disable();
        http.csrf().disable();
        return http.build();
    }

    private ServerAuthenticationConverter createServerAuthenticationConverter() {
        DelegatingGatewayServerAuthenticationConverter.Builder builder = DelegatingGatewayServerAuthenticationConverter.builder();
        SystemWebGatewayServerAuthenticationConverter systemWeb = new SystemWebGatewayServerAuthenticationConverter();
        builder.add(systemWeb);
        return builder.build();
    }

    private ReactiveAuthenticationManagerResolver<ServerWebExchange> createReactiveAuthenticationManagerResolver() {
        return new DelegatingGatewayReactiveAuthenticationManagerResolver(authenticationManagerList);
    }
}
