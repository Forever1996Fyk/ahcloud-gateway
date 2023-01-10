package com.ahcloud.gateway.server.infrastructure.security;

import cn.hutool.core.util.ArrayUtil;
import com.google.common.collect.Lists;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

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

    private final AuthorizationManager authorizationManager;

    public OAuth2ResourceServerConfiguration(AuthorizationManager authorizationManager) {
        this.authorizationManager = authorizationManager;
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
//        // 自定义错误端点
//        http.oauth2ResourceServer()
//                .authenticationEntryPoint(null);
//        List<String>  ignoreUriList = Lists.newArrayList();
//        http.authorizeExchange()
//                // 放行 uri集合
//                .pathMatchers(ArrayUtil.toArray(ignoreUriList, String.class)).permitAll()
//                .anyExchange().access(authorizationManager)
//                .and().exceptionHandling()
//                .accessDeniedHandler((exchange, denied) -> null)
//                .authenticationEntryPoint((exchange, e) -> null)
//                .and().csrf().disable();
//        return http.build();
        http.csrf().disable();
        return http.build();
    }

}
