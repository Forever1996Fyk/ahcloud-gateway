package com.ahcloud.common.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @program: ahcloud-common
 * @description:
 * @author: YuKai Fan
 * @create: 2022/12/16 10:40
 **/
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RpcResult<T> implements Result, Serializable {
    private static final long serialVersionUID = -5020595658618133322L;
    /**
     * 返回码
     */
    private int code;

    /**
     * 返回信息
     */
    private String message;

    /**
     * 返回数据
     */
    private T data;

    /**
     * 是否成功
     */
    private boolean success;

    public static RpcResult<Void> ofSuccess() {
        return ofSuccess(null);
    }

    public static <T> RpcResult<T> ofSuccess(T data) {
        return ofSuccess(SUCCESS_CODE, SUCCESS_MESSAGE, data);
    }

    public static RpcResult<Void> ofSuccess(int code, String message) {
        return ofSuccess(code, message, null);
    }

    public static <T> RpcResult<T> ofSuccess(int code, String message, T data) {
        return RpcResult.<T>builder()
                .code(code)
                .message(message)
                .data(data)
                .success(true)
                .build();
    }

    public static <T> RpcResult<T> ofFailedGeneric(int code, String message) {
        return RpcResult.<T>builder()
                .code(code)
                .message(message)
                .success(false)
                .build();
    }

    public static RpcResult<Void> ofFailed(int code, String message) {
        return RpcResult.<Void>builder()
                .code(code)
                .message(message)
                .success(false)
                .build();
    }

    public static RpcResult<Void> ofFailed() {
        return ofFailed(FAILED_CODE, FAILED_MESSAGE);
    }

    public boolean isFailed() {
        return !success;
    }
}
