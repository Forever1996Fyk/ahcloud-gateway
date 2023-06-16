package com.ahcloud.gateway.scg.common.exception;

import com.ahcloud.common.enums.ErrorCode;
import com.ahcloud.common.exception.BaseException;
import org.springframework.web.server.ServerWebExchange;

/**
 * @program: ahcloud-gateway
 * @description: 网关黑名单异常
 * @author: YuKai Fan
 * @create: 2023/6/15 17:33
 **/
public class GatewayBlackException extends BaseException {
    private static final long serialVersionUID = 1799479659918286716L;

    /**
     * 错误码
     */
    private final ErrorCode errorCode;
    private final String errorMessage;

    /**
     * 请求属性
     */
    private final ServerWebExchange exchange;

    public GatewayBlackException(ServerWebExchange exchange, ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getMessage();
        this.exchange = exchange;
    }

    public GatewayBlackException(ServerWebExchange exchange, ErrorCode errorCode, String... args) {
        super(errorCode, args);
        this.exchange = exchange;
        this.errorMessage = appendErrorMsg(errorCode, args);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public ServerWebExchange getExchange() {
        return exchange;
    }
}
