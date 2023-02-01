package com.ahcloud.gateway.server.infrastructure.exception;

import com.ahcloud.common.enums.ErrorCode;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/1/16 21:03
 **/
public class GatewayException extends RuntimeException {

    private ErrorCode errorCode;

    private String message;

    public GatewayException(String message) {
        super(message);
        this.message = message;
    }

    public GatewayException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
