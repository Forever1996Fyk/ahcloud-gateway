package com.ahcloud.gateway.server.infrastructure.rpc;

import com.ahcloud.gateway.server.domain.admin.bo.BackstageUserAuthenticationBO;
import com.ahcloud.gateway.server.domain.app.AppUserAuthenticationBO;

/**
 * @program: ahcloud-gateway-open
 * @description:
 * @author: YuKai Fan
 * @create: 2024/3/8 17:14
 **/
public interface RpcOrHttpRemoteService {

    /**
     * 获取app用户认证信息
     * @param token
     * @return
     */
    AppUserAuthenticationBO getAppUserAuthenticationByToken(String token);

    /**
     * 获取后台用户认证信息
     * @param token
     * @return
     */
    BackstageUserAuthenticationBO getAdminUserAuthenticationByToken(String token);
}
