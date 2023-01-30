package com.ahcloud.gateway.client;

import com.ahcloud.gateway.client.common.ErrorCode;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/1/16 17:41
 **/
public enum GatewayRetCodeEnum implements ErrorCode {

    /**
     * http status
     */
    CERTIFICATE_EXPIRED_ERROR(401, "用户认证凭证已过期"),
    CERTIFICATE_EXCEPTION_ERROR(401, "用户认证凭证异常"),

    /**
     * 公共响应码
     */
    SYSTEM_ERROR(1_0_100_1001, "系统异常"),

    /**
     * 响应错误码
     */
    RESPONSE_BODY_CONVERT_FAILED(2_0_100_1001, "响应体转换失败"),

    /**
     * 认证错误码
     */
    AUTHENTICATION_USER_PRINCIPAL_ERROR(3_0_100_1001, "用户认证信息错误"),
    AUTHENTICATION_TOKEN_ERROR(3_0_100_1002, "用户认证凭证错误"),
    AUTHENTICATION_AUTHORITY_ERROR(3_0_100_1004, "用户认证权限错误"),

    ;

    private final int code;
    private final String message;

    GatewayRetCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
