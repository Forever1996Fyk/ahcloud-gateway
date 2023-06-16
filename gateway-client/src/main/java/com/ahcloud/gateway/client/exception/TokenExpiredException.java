package com.ahcloud.gateway.client.exception;

import com.ahcloud.common.exception.BaseException;
import com.ahcloud.gateway.client.enums.GatewayRetCodeEnum;

/**
 * @Description 业务异常
 * @Author yin.jinbiao
 * @Date 2021/9/27 14:26
 * @Version 1.0
 */
public class TokenExpiredException extends BaseException {
    private static final long serialVersionUID = 8273298480763571082L;

    public TokenExpiredException() {
        super(GatewayRetCodeEnum.CERTIFICATE_EXPIRED_ERROR);
    }
}
