package com.ahcloud.gateway.server.infrastructure.security.token.authentication;

import com.ahcloud.gateway.server.infrastructure.security.token.authentication.bo.AccessTokenBO;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/2/1 09:06
 **/
public interface UserReactiveAuthentication {

    /**
     * 用户id
     * @return
     */
    Long getUserId();

    /**
     * 租户id
     * @return
     */
    Long getTenantId();

    /**
     * token信息
     * @return
     */
    AccessTokenBO getAccessTokenBO();
}
