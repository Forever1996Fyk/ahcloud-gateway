package com.ahcloud.common.exception;

import com.ahcloud.common.enums.ErrorCode;

/**
 * @program: ahcloud-common
 * @description:
 * @author: YuKai Fan
 * @create: 2023/1/29 17:36
 **/
public abstract class BaseException extends RuntimeException {


    public BaseException(ErrorCode errorCode, String... args) {
        super(appendErrorMsg(errorCode, args));
    }

    public BaseException(String message) {
        super(message);
    }

    /**
     * 拼接错误信息
     *
     * @param errorCode
     * @param args
     * @return
     */
    protected static String appendErrorMsg(ErrorCode errorCode, String... args) {
        String errorMsg = String.format(errorCode.getMessage(), args);
        // 说明StringFormat不启作用
        if (errorMsg != null && !errorMsg.equals(errorCode.getMessage())) {
            StringBuffer sb = new StringBuffer();
            sb.append(errorCode.getMessage()).append("，详情：");
            if (args != null && args.length > 0) {
                for (String arg : args) {
                    sb.append(arg).append(" ");
                }
            }
            errorMsg = sb.toString();
        }
        return errorMsg;
    }
}
