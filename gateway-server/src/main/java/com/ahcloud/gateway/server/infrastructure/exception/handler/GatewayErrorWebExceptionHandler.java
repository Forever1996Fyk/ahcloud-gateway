package com.ahcloud.gateway.server.infrastructure.exception.handler;

import com.ahcloud.gateway.scg.common.exception.handler.ExceptionRequestHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @program: ahcloud-gateway
 * @description: 网关统一异常处理
 * 这里处理的异常为网关级的异常，也即网关直接抛出的异常，如请求的路由不存在或网关有内部错误，对于网关代理的下层服务返回的异常，不会进行处理，
 * 如下层服务返回了404，对网关来讲，这个请求是没问题的，网关不会作为异常去捕获处理，因为网关只是转发，
 * 且下层服务返回的异常应该是已经经过下层服务本身依据自身需求处理过的了，无需网关再处理。
 * @author: YuKai Fan
 * @create: 2023/1/16 16:55
 **/
@Slf4j
public class GatewayErrorWebExceptionHandler implements ErrorWebExceptionHandler {

    private final ExceptionRequestHandler exceptionRequestHandler;

    public GatewayErrorWebExceptionHandler(ExceptionRequestHandler exceptionRequestHandler) {
        this.exceptionRequestHandler = exceptionRequestHandler;
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ServerHttpResponse response = exchange.getResponse();
        if (response.isCommitted()) {
            return Mono.error(ex);
        }
        return exceptionRequestHandler.handleRequest(exchange, ex);
    }
}
