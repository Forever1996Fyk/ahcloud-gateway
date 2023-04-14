package com.ahcloud.gateway.server.infrastructure.security.token.service.impl;

import com.ahcloud.common.utils.DateUtils;
import com.ahcloud.gateway.client.enums.GatewayRetCodeEnum;
import com.ahcloud.gateway.server.domain.admin.bo.AdminAccessTokenBO;
import com.ahcloud.gateway.server.domain.admin.bo.AdminUserAuthenticationBO;
import com.ahcloud.gateway.server.infrastructure.exception.GatewayAuthenticationException;
import com.ahcloud.gateway.server.infrastructure.rpc.AdminRpcService;
import com.ahcloud.gateway.server.infrastructure.security.token.authentication.bo.AccessTokenBO;
import com.ahcloud.gateway.server.infrastructure.security.token.authentication.bo.AdminUserReactiveAuthenticationBO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

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

    @Override
    protected AdminUserReactiveAuthenticationBO createUserReactiveAuthentication(String token) {
        AdminUserAuthenticationBO authentication = adminRpcService.getAdminUserAuthenticationByToken(token);
        AdminAccessTokenBO accessTokenBO = authentication.getAccessTokenBO();
        if (Objects.isNull(accessTokenBO)) {
            throw new GatewayAuthenticationException(GatewayRetCodeEnum.CERTIFICATE_EXCEPTION_ERROR);
        }
        return AdminUserReactiveAuthenticationBO.builder()
                .scopes(authentication.getScopes())
                .userId(authentication.getUserId())
                .authorities(authentication.getAuthorities())
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
