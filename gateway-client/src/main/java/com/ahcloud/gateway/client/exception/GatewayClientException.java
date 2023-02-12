package com.ahcloud.gateway.client.exception;

import com.ahcloud.common.enums.ErrorCode;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/1/16 21:03
 **/
public class GatewayClientException extends RuntimeException {

    private static final long serialVersionUID = -7413290821971758672L;

    public GatewayClientException(String message) {
        super(message);
    }
}
