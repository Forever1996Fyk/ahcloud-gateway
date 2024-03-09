package com.ahcloud.common.enums;

/**
 * @program: ahcloud-common
 * @description:
 * @author: YuKai Fan
 * @create: 2022/12/16 11:33
 **/
public interface ErrorCode {

    /**
     * 获取错误码
     * @return 错误码
     */
    int getCode();

    /**
     * 获取错误信息
     * @return 错误信息
     */
    String getMessage();

    /**
     * 默认构建错误码
     * @param code
     * @param message
     * @return
     */
    static ErrorCode build(int code, String message) {
        return new ErrorCode() {
            @Override
            public int getCode() {
                return code;
            }

            @Override
            public String getMessage() {
                return message;
            }
        };
    }
}
