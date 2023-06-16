package com.ahcloud.gateway.server.infrastructure.exception.handler;

import com.ahcloud.common.enums.ErrorCode;
import com.ahcloud.gateway.client.enums.GatewayRetCodeEnum;
import com.ahcloud.gateway.client.exception.BizException;
import com.ahcloud.gateway.client.exception.TokenExpiredException;
import com.ahcloud.gateway.scg.common.exception.handler.AbstractExceptionRequestHandler;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/6/15 16:51
 **/
public class TokenExceptionRequestHandler extends AbstractExceptionRequestHandler<BizException> {
    @Override
    public boolean support(Throwable t) {
        return t instanceof TokenExpiredException;
    }

    @Override
    protected ErrorCode extract(BizException ex) {
        return GatewayRetCodeEnum.CERTIFICATE_EXCEPTION_ERROR;
    }
}
