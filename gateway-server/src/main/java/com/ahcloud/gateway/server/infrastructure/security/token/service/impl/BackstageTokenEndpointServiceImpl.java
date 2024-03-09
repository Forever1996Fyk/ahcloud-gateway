package com.ahcloud.gateway.server.infrastructure.security.token.service.impl;

import com.ahcloud.common.utils.DateUtils;
import com.ahcloud.gateway.client.enums.GatewayRetCodeEnum;
import com.ahcloud.gateway.server.domain.admin.bo.BackstageAccessTokenBO;
import com.ahcloud.gateway.server.domain.admin.bo.BackstageUserAuthenticationBO;
import com.ahcloud.gateway.server.infrastructure.exception.GatewayAuthenticationException;
import com.ahcloud.gateway.server.infrastructure.rpc.RpcOrHttpRemoteService;
import com.ahcloud.gateway.server.infrastructure.security.token.authentication.bo.AccessTokenBO;
import com.ahcloud.gateway.server.infrastructure.security.token.authentication.bo.BackstageUserReactiveAuthenticationBO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @program: ahcloud-gateway
 * @description: 后台token认证接口
 * @author: YuKai Fan
 * @create: 2023/1/31 17:32
 **/
@Slf4j
@Service(value = "backstageTokenEndpointService")
public class BackstageTokenEndpointServiceImpl extends CacheTokenEndpointService<BackstageUserReactiveAuthenticationBO> {


    private final RpcOrHttpRemoteService rpcOrHttpRemoteService;

    private final static String LOG_MARK = "BackstageTokenEndpointServiceImpl";


    public BackstageTokenEndpointServiceImpl(RpcOrHttpRemoteService rpcOrHttpRemoteService) {
        this.rpcOrHttpRemoteService = rpcOrHttpRemoteService;
    }

    @Override
    protected BackstageUserReactiveAuthenticationBO createUserReactiveAuthentication(String token) {
        BackstageUserAuthenticationBO authentication = rpcOrHttpRemoteService.getAdminUserAuthenticationByToken(token);
        BackstageAccessTokenBO accessTokenBO = authentication.getAccessTokenBO();
        if (Objects.isNull(accessTokenBO)) {
            throw new GatewayAuthenticationException(GatewayRetCodeEnum.CERTIFICATE_EXCEPTION_ERROR);
        }
        return BackstageUserReactiveAuthenticationBO.builder()
                .scopes(authentication.getScopes())
                .userId(authentication.getUserId())
                .authorities(authentication.getAuthorities())
                .userName(authentication.getUserName())
                .account(authentication.getAccount())
                .nickName(authentication.getNickName())
                .accessTokenBO(
                        AccessTokenBO.builder()
                                .token(accessTokenBO.getToken())
                                .expireAt(DateUtils.dateToInstant(accessTokenBO.getExpireTime()))
                                .issuedAt(DateUtils.dateToInstant(accessTokenBO.getIssuedTime()))
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
