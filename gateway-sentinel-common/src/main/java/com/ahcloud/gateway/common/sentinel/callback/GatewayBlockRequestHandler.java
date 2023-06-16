package com.ahcloud.gateway.common.sentinel.callback;

import com.ahcloud.common.result.ResponseResult;
import com.ahcloud.gateway.client.enums.GatewayRetCodeEnum;
import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.BlockRequestHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/6/14 16:54
 **/
public class GatewayBlockRequestHandler implements BlockRequestHandler {

    @Override
    public Mono<ServerResponse> handleRequest(ServerWebExchange serverWebExchange, Throwable throwable) {
        ResponseResult<Void> result = ResponseResult.ofFailed(GatewayRetCodeEnum.SERVER_BUSY.getCode(), GatewayRetCodeEnum.SERVER_BUSY.getMessage());
        return ServerResponse.status(HttpStatus.NOT_ACCEPTABLE)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(result));
    }
}
