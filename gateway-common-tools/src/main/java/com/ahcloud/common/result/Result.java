package com.ahcloud.common.result;

/**
 * @program: ahcloud-common
 * @description:
 * @author: YuKai Fan
 * @create: 2022/12/16 10:45
 **/
public interface Result {

    int SUCCESS_CODE = 0;

    String SUCCESS_MESSAGE = "成功";

    int FAILED_CODE = 500;

    String FAILED_MESSAGE = "系统错误";

    /**
     * 编码
     * @return
     */
    int getCode();

    /**
     * 消息
     * @return
     */
    String getMessage();
}
