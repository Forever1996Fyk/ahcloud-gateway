package com.ahcloud.gateway.server.infrastructure.config;

import com.ahcloud.gateway.scg.common.exception.handler.DelegatingExceptionRequestHandler;
import com.ahcloud.gateway.server.infrastructure.exception.handler.BizExceptionRequestHandler;
import com.ahcloud.gateway.server.infrastructure.exception.handler.GatewayBlackExceptionRequestHandler;
import com.ahcloud.gateway.server.infrastructure.exception.handler.GatewayErrorWebExceptionHandler;
import com.ahcloud.gateway.server.infrastructure.exception.handler.GatewayExceptionRequestHandler;
import com.ahcloud.gateway.server.infrastructure.exception.handler.ResponseStatusExceptionRequestHandler;
import com.ahcloud.gateway.server.infrastructure.exception.handler.TokenExceptionRequestHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/6/16 09:24
 **/
@Slf4j
@Configuration
public class ExceptionConfiguration {

    @Bean
    public GatewayErrorWebExceptionHandler errorWebExceptionHandler(ApplicationEventPublisher publisher) {
        DelegatingExceptionRequestHandler delegatingExceptionRequestHandler = new DelegatingExceptionRequestHandler();
        delegatingExceptionRequestHandler.addHandler(new BizExceptionRequestHandler());
        delegatingExceptionRequestHandler.addHandler(new GatewayExceptionRequestHandler());
        delegatingExceptionRequestHandler.addHandler(new ResponseStatusExceptionRequestHandler());
        delegatingExceptionRequestHandler.addHandler(new TokenExceptionRequestHandler());
        delegatingExceptionRequestHandler.addHandler(new GatewayBlackExceptionRequestHandler(publisher));
        return new GatewayErrorWebExceptionHandler(delegatingExceptionRequestHandler);
    }
}
