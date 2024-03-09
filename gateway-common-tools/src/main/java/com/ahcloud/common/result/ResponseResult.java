package com.ahcloud.common.result;

import com.ahcloud.common.enums.ErrorCode;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.skywalking.apm.toolkit.trace.TraceContext;

/**
 * @program: ahcloud-common
 * @description:
 * @author: YuKai Fan
 * @create: 2022/12/16 10:39
 **/
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ResponseResult<T> implements Result {

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

    /**
     * traceId
     */
    private String traceId;

    public static ResponseResult<Void> ofSuccess() {
        return ofSuccess(null);
    }

    public static <T> ResponseResult<T> ofSuccess(T data) {
        return ofSuccess(SUCCESS_CODE, SUCCESS_MESSAGE, data);
    }

    public static ResponseResult<Void> ofSuccess(int code, String message) {
        return ofSuccess(code, message, null);
    }

    public static <T> ResponseResult<T> ofSuccess(int code, String message, T data) {
        return ResponseResult.<T>builder()
                .code(code)
                .message(message)
                .data(data)
                .success(true)
                .traceId(TraceContext.traceId())
                .build();
    }

    public static ResponseResult<Void> ofFailed(int code, String message) {
        return ResponseResult.<Void>builder()
                .code(code)
                .message(message)
                .success(false)
                .traceId(TraceContext.traceId())
                .build();
    }

    public static ResponseResult<Void> ofFailed(ErrorCode errorCode) {
        return ResponseResult.<Void>builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .success(false)
                .traceId(TraceContext.traceId())
                .build();
    }

    public static ResponseResult<Void> ofFailed() {
        return ofFailed(FAILED_CODE, FAILED_MESSAGE);
    }

    @JsonIgnore
    public boolean isFailed() {
        return !success;
    }

}
