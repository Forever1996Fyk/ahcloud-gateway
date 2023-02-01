package com.ahcloud.gateway.server.infrastructure.exception;

import com.ahcloud.common.enums.ErrorCode;
import org.springframework.security.access.AccessDeniedException;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/1/17 10:24
 **/
public class GatewayAccessDeniedException extends AccessDeniedException {

    private final ErrorCode errorCode;

    public GatewayAccessDeniedException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public GatewayAccessDeniedException(ErrorCode errorCode, Throwable t) {
        super(errorCode.getMessage(), t);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
