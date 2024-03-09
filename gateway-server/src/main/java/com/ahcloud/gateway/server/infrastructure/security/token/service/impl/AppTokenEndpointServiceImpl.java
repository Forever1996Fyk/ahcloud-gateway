package com.ahcloud.gateway.server.infrastructure.security.token.service.impl;

import com.ahcloud.common.utils.DateUtils;
import com.ahcloud.gateway.client.enums.GatewayRetCodeEnum;
import com.ahcloud.gateway.server.domain.app.AppAccessTokenBO;
import com.ahcloud.gateway.server.domain.app.AppUserAuthenticationBO;
import com.ahcloud.gateway.server.infrastructure.exception.GatewayAuthenticationException;
import com.ahcloud.gateway.server.infrastructure.rpc.RpcOrHttpRemoteService;
import com.ahcloud.gateway.server.infrastructure.security.token.authentication.bo.AccessTokenBO;
import com.ahcloud.gateway.server.infrastructure.security.token.authentication.bo.AppUserReactiveAuthenticationBO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @program: ahcloud-gateway
 * @description: APP token认证接口
 * @author: YuKai Fan
 * @create: 2023/1/31 17:32
 **/
@Slf4j
@Service(value = "appTokenEndpointService")
public class AppTokenEndpointServiceImpl extends CacheTokenEndpointService<AppUserReactiveAuthenticationBO> {

    private final RpcOrHttpRemoteService rpcOrHttpRemoteService;
    private final static String LOG_MARK = "AppTokenEndpointServiceImpl";

    public AppTokenEndpointServiceImpl(RpcOrHttpRemoteService rpcOrHttpRemoteService) {
        this.rpcOrHttpRemoteService = rpcOrHttpRemoteService;
    }

    @Override
    protected AppUserReactiveAuthenticationBO createUserReactiveAuthentication(String token) {
        AppUserAuthenticationBO authentication = rpcOrHttpRemoteService.getAppUserAuthenticationByToken(token);
        AppAccessTokenBO accessTokenBO = authentication.getAccessTokenBO();
        if (Objects.isNull(accessTokenBO)) {
            throw new GatewayAuthenticationException(GatewayRetCodeEnum.CERTIFICATE_EXCEPTION_ERROR);
        }
        return AppUserReactiveAuthenticationBO.builder()
                .scopes(authentication.getScopes())
                .userId(authentication.getUserId())
                .accessTokenBO(
                        AccessTokenBO.builder()
                                .token(accessTokenBO.getToken())
                                .expireAt(DateUtils.dateToInstant(accessTokenBO.getExpireAt()))
                                .issuedAt(DateUtils.dateToInstant(accessTokenBO.getIssuedAt()))
                                .expiresIn(accessTokenBO.getExpiresIn())
                                .build()
                )
                .build();
    }

    @Override
    protected String getLogMark() {
        return LOG_MARK;
    }
}
