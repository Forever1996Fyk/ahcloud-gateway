package com.ahcloud.gateway.server.infrastructure.security.token.service.impl;

import com.ahcloud.gateway.server.domain.admin.bo.AdminUserAuthenticationBO;
import com.ahcloud.gateway.server.infrastructure.security.token.authentication.bo.AccessTokenBO;
import com.ahcloud.gateway.server.infrastructure.security.token.authentication.bo.AdminUserReactiveAuthenticationBO;
import com.ahcloud.gateway.server.infrastructure.rpc.AdminRpcService;
import lombok.extern.slf4j.Slf4j;
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
@Service(value = "adminTokenEndpointService")
public class AdminTokenEndpointServiceImpl extends CacheTokenEndpointService<AdminUserReactiveAuthenticationBO> {
    @Resource
    private AdminRpcService adminRpcService;

    private final static String LOG_MARK = "AdminTokenEndpointServiceImpl";

    @Autowired
    public AdminTokenEndpointServiceImpl(RedisTemplate<String, Object> redisTemplate) {
        super(redisTemplate);
    }

    @Override
    protected AdminUserReactiveAuthenticationBO createUserReactiveAuthentication(String token) {
        AdminUserAuthenticationBO authentication = adminRpcService.getAdminUserAuthenticationByToken(token);
        return AdminUserReactiveAuthenticationBO.builder()
                .scopes(authentication.getScopes())
                .userId(authentication.getUserId())
                .authorities(authentication.getAuthorities())
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
