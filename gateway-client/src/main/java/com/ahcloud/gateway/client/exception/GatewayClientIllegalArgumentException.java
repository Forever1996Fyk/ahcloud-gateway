package com.ahcloud.gateway.client.exception;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/1/16 21:03
 **/
public class GatewayClientIllegalArgumentException extends IllegalArgumentException {

    private static final long serialVersionUID = 5847484559275352083L;

    public GatewayClientIllegalArgumentException(String message) {
        super(message);
    }
}
