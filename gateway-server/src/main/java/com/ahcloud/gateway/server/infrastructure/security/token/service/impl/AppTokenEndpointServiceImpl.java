package com.ahcloud.gateway.server.infrastructure.security.token.service.impl;

import com.ahcloud.gateway.server.domain.admin.bo.AdminUserAuthenticationBO;
import com.ahcloud.gateway.server.domain.app.AppUserAuthenticationBO;
import com.ahcloud.gateway.server.infrastructure.rpc.AdminRpcService;
import com.ahcloud.gateway.server.infrastructure.rpc.AppUserRpcService;
import com.ahcloud.gateway.server.infrastructure.security.token.authentication.bo.AccessTokenBO;
import com.ahcloud.gateway.server.infrastructure.security.token.authentication.bo.AdminUserReactiveAuthenticationBO;
import com.ahcloud.gateway.server.infrastructure.security.token.authentication.bo.AppUserReactiveAuthenticationBO;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/1/31 17:32
 **/
@Slf4j
@Service(value = "appTokenEndpointService")
public class AppTokenEndpointServiceImpl extends CacheTokenEndpointService<AppUserReactiveAuthenticationBO> {
    @Resource
    private AppUserRpcService appUserRpcService;

    private final static String LOG_MARK = "AppTokenEndpointServiceImpl";

    @Autowired
    public AppTokenEndpointServiceImpl(RedisTemplate<String, Object> redisTemplate) {
        super(redisTemplate);
    }

    @Override
    protected AppUserReactiveAuthenticationBO createUserReactiveAuthentication(String token) {
        AppUserAuthenticationBO authentication = appUserRpcService.getAppUserAuthenticationByToken(token);
        return AppUserReactiveAuthenticationBO.builder()
                .scopes(authentication.getScopes())
                .userId(authentication.getUserId())
                .accessTokenBO(
                        AccessTokenBO.builder()
                                .token(authentication.getAccessTokenBO().getToken())
                                .expireTime(authentication.getAccessTokenBO().getExpireAt())
                                .issuedTime(authentication.getAccessTokenBO().getIssuedAt())
                                .build()
                )
                .build();
    }

    @Override
    protected String getLogMark() {
        return LOG_MARK;
    }
}
