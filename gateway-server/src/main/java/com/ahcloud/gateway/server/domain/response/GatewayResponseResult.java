package com.ahcloud.gateway.server.domain.response;

import com.ahcloud.gateway.client.GatewayRetCodeEnum;
import com.ahcloud.gateway.client.common.ErrorCode;
import com.ahcloud.kernel.core.common.Constant;
import lombok.*;

/**
 * @program: ahcloud-gateway
 * @description: API 返回统一返回体
 * @author: YuKai Fan
 * @create: 2023/1/15 22:42
 **/
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class GatewayResponseResult {

    /**
     * 错误码
     */
    private int code;

    /**
     * 返回结果
     */
    private Object result;

    /**
     * 错误信息
     */
    private String message;

    /**
     * 网关请求是否成功
     */
    private boolean success;

    /**
     * traceId
     */
    private String traceId;

    public static GatewayResponseResult ofSuccess(Object result) {
        return GatewayResponseResult.builder()
                .result(result)
                .success(true)
                .code(0)
                .traceId(Constant.CTX_KEY_TRACE_ID.get())
                .build();
    }

    public static GatewayResponseResult ofSuccess() {
        return GatewayResponseResult.builder()
                .success(true)
                .code(0)
                .traceId(Constant.CTX_KEY_TRACE_ID.get())
                .build();
    }

    public static GatewayResponseResult ofFailed(int code, String message) {
        return GatewayResponseResult.builder()
                .success(false)
                .message(message)
                .code(code)
                .traceId(Constant.CTX_KEY_TRACE_ID.get())
                .build();
    }

    public static GatewayResponseResult ofFailed(ErrorCode errorCode) {
        return ofFailed(errorCode.getCode(), errorCode.getMessage());
    }


    public static GatewayResponseResult ofFailed() {
        return ofFailed(GatewayRetCodeEnum.SYSTEM_ERROR);
    }
}
