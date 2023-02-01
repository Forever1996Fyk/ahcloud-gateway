package com.ahcloud.gateway.server.infrastructure.security.token.service;

import com.ahcloud.gateway.server.infrastructure.security.token.authentication.UserReactiveAuthentication;

/**
 * @program: ahcloud-gateway
 * @description: token端点服务
 * @author: YuKai Fan
 * @create: 2023/1/31 16:25
 **/
public interface TokenEndpointService<T extends UserReactiveAuthentication> {

    /**
     * 通过token认证
     * @param token
     * @param prefix
     * @return
     */
     T authenticationByToken(String token, String prefix);
}
