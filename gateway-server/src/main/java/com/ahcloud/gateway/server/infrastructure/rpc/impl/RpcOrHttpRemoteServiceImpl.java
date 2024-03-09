package com.ahcloud.gateway.server.infrastructure.rpc.impl;

import com.ahcloud.gateway.server.domain.admin.bo.BackstageUserAuthenticationBO;
import com.ahcloud.gateway.server.domain.app.AppUserAuthenticationBO;
import com.ahcloud.gateway.server.infrastructure.rpc.RpcOrHttpRemoteService;
import org.springframework.stereotype.Component;

/**
 * @program: ahcloud-gateway-open
 * @description: 这里需要自定义实现自己项目中的认证逻辑，可以是rpc，也可以是http，这里暂时写死
 * @author: YuKai Fan
 * @create: 2024/3/8 17:16
 **/
@Component
public class RpcOrHttpRemoteServiceImpl implements RpcOrHttpRemoteService {

    @Override
    public AppUserAuthenticationBO getAppUserAuthenticationByToken(String token) {
        return AppUserAuthenticationBO.builder().build();
    }

    @Override
    public BackstageUserAuthenticationBO getAdminUserAuthenticationByToken(String token) {
        return BackstageUserAuthenticationBO.builder().build();
    }
}
