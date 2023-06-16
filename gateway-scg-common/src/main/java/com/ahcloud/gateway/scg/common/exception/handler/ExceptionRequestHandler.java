package com.ahcloud.gateway.scg.common.exception.handler;

import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/6/15 16:12
 **/
public interface ExceptionRequestHandler {

    /**
     * 处理异常
     * @param exchange
     * @param t
     * @return
     */
    Mono<Void> handleRequest(ServerWebExchange exchange, Throwable t);

    /**
     * 支持哪种异常类型
     * @param t
     * @return
     */
    boolean support(Throwable t);
}
