package com.ahcloud.gateway.core.infrastructure.exception;

import com.ahcloud.common.enums.ErrorCode;
import com.ahcloud.common.exception.BaseException;

/**
 * @Description 业务异常
 * @Author yin.jinbiao
 * @Date 2021/9/27 14:26
 * @Version 1.0
 */
public class BizException extends BaseException {

    /**
     * 错误码
     */
    private ErrorCode errorCode;

    private String errorMessage;

    public BizException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getMessage();
    }

    public BizException(ErrorCode errorCode, String... args) {
        super(errorCode, args);
        this.errorMessage = appendErrorMsg(errorCode, args);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public int getCode() {
        return this.errorCode.getCode();
    }
}
