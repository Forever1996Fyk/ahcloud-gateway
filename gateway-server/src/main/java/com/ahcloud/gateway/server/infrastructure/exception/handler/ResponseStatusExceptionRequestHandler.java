package com.ahcloud.gateway.server.infrastructure.exception.handler;

import com.ahcloud.common.enums.ErrorCode;
import com.ahcloud.gateway.client.exception.BizException;
import com.ahcloud.gateway.scg.common.exception.handler.AbstractExceptionRequestHandler;
import org.springframework.web.server.ResponseStatusException;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/6/15 16:51
 **/
public class ResponseStatusExceptionRequestHandler extends AbstractExceptionRequestHandler<ResponseStatusException> {
    @Override
    public boolean support(Throwable t) {
        return t instanceof ResponseStatusException;
    }

    @Override
    protected ErrorCode extract(ResponseStatusException ex) {
        return new ErrorCode() {
            @Override
            public int getCode() {
                return ex.getStatus().value();
            }
            @Override
            public String getMessage() {
                return ex.getMessage();
            }
        };
    }
}
