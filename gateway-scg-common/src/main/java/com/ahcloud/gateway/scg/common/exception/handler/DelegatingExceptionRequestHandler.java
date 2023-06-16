package com.ahcloud.gateway.scg.common.exception.handler;

import com.google.common.collect.Lists;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/6/16 09:21
 **/
public class DelegatingExceptionRequestHandler implements ExceptionRequestHandler {

    private final List<ExceptionRequestHandler> handlerList = Lists.newLinkedList();

    public void addHandler(ExceptionRequestHandler handler) {
        handlerList.add(handler);
    }

    @Override
    public Mono<Void> handleRequest(ServerWebExchange exchange, Throwable ex) {
        return handlerList.stream().filter(handler -> handler.support(ex))
                .findFirst()
                .map(exceptionRequestHandler -> exceptionRequestHandler.handleRequest(exchange, ex))
                .orElse(Mono.empty());
    }

    @Override
    public boolean support(Throwable t) {
        return true;
    }
}
