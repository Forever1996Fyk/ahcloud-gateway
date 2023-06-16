package com.ahcloud.gateway.server.infrastructure.exception.handler;

import com.ahcloud.common.enums.ErrorCode;
import com.ahcloud.gateway.client.exception.GatewayException;
import com.ahcloud.gateway.scg.common.exception.handler.AbstractExceptionRequestHandler;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/6/15 16:49
 **/
public class GatewayExceptionRequestHandler extends AbstractExceptionRequestHandler<GatewayException> {


    @Override
    public boolean support(Throwable t) {
        return t instanceof GatewayException;
    }

    @Override
    protected ErrorCode extract(GatewayException ex) {
        return ex.getErrorCode();
    }
}
