package com.ahcloud.gateway.client.common;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/1/16 17:40
 **/
public interface ErrorCode {

    /**
     * 返回码
     * @return
     */
    int getCode();

    /**
     * 返回信息
     * @return
     */
    String getMessage();
}
