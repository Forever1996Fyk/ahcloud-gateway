package com.ahcloud.gateway.server.infrastructure.exception;

import com.ahcloud.gateway.client.common.ErrorCode;
import org.springframework.security.core.AuthenticationException;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/1/17 10:26
 **/
public class GatewayAuthenticationException extends AuthenticationException {

    private final ErrorCode errorCode;

    public GatewayAuthenticationException(ErrorCode errorCode, Throwable t) {
        super(errorCode.getMessage(), t);
        this.errorCode = errorCode;
    }

    public GatewayAuthenticationException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
