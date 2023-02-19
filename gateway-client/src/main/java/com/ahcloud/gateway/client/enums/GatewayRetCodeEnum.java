package com.ahcloud.gateway.client.enums;


import com.ahcloud.common.enums.ErrorCode;

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
    CERTIFICATE_EXCEPTION_ERROR(401_1, "用户认证凭证异常"),

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

    GATEWAY_USER_AUTHENTICATION_FAILED(4_0_100_1001, "用户认证失败"),

    /**
     * 授权
     */
    AUTHORIZATION_DISABLED(5_0_100_1001, "权限不可用"),
    AUTHORIZATION_NOT_OWNED(5_0_100_1002, "暂无权限"),
    AUTHORIZATION_MARK_ERROR(5_0_100_1003, "权限错误, 请联系管理员"),
    AUTHORIZATION_GET_ERROR(5_0_100_1004, "获取权限失败, 请联系管理员"),

    /**
     * 网关接口
     */
    GATEWAY_API_CODE_EXITED(5_0_100_1001, "当前api编码已存在"),
    GATEWAY_API_NOT_EXITED(5_0_100_1002, "当前api不存在"),
    GATEWAY_API_ADD_FAILED(5_0_100_1003, "接口新增失败"),
    GATEWAY_API_UPDATE_FAILED(5_0_100_1004, "接口更新失败"),
    GATEWAY_API_DELETE_FAILED(5_0_100_1005, "接口删除失败"),

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
