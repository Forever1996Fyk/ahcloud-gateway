package com.ahcloud.gateway.scg.common.exception.handler;

import com.ahcloud.common.enums.ErrorCode;
import com.ahcloud.common.result.ResponseResult;
import com.ahcloud.common.utils.JsonUtils;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/6/15 16:29
 **/
@Slf4j
public abstract class AbstractExceptionRequestHandler<T extends Throwable> implements ExceptionRequestHandler {

    @Override
    public Mono<Void> handleRequest(ServerWebExchange exchange, Throwable ex) {
        ServerHttpResponse response = exchange.getResponse();
        // 将返回格式设为json
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        T t = (T) ex;
        ErrorCode errorCode = extract(t);
        ResponseResult<Void> result = ResponseResult.ofFailed(errorCode.getCode(), errorCode.getMessage());
        afterHandle2(t);
        return response.writeWith(Mono.fromSupplier(() -> {
            DataBufferFactory bufferFactory = response.bufferFactory();
            try {
                return bufferFactory.wrap(JsonUtils.beanToByte(result));
            } catch (Exception e) {
                log.error("GatewayErrorWebExceptionHandler[handle] exception convert to GatewayResponseResult failed,  exception is {}", Throwables.getStackTraceAsString(e));
                return bufferFactory.wrap(new byte[0]);
            }
        }));
//                .then(Mono.defer(() -> afterHandle(t)));
    }

    /**
     * 后置处理
     * @param t
     * @return
     */
    protected Mono<Void> afterHandle(T t) {
        return Mono.empty();
    }

    /**
     * 后置处理
     * @param t
     * @return
     */
    protected void afterHandle2(T t) {

    }

    /**
     * 提取errorCode
     * @param ex
     * @param ex
     * @return
     */
    protected abstract ErrorCode extract(T ex);

}
