package com.ahcloud.gateway.server.application.helper;

import com.ahcloud.admin.client.domain.dubbo.token.AccessTokenDTO;
import com.ahcloud.admin.client.domain.dubbo.token.AdminUserAuthenticationDTO;
import com.ahcloud.gateway.server.domain.admin.bo.AdminAccessTokenBO;
import com.ahcloud.gateway.server.domain.admin.bo.AdminUserAuthenticationBO;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/1/31 17:15
 **/
public class AdminUserAuthenticationHelper {

    /**
     * 数据转换
     * @param userAuthenticationDTO
     * @return
     */
    public static AdminUserAuthenticationBO convertBO(AdminUserAuthenticationDTO userAuthenticationDTO) {
        AccessTokenDTO accessTokenDTO = userAuthenticationDTO.getAccessTokenDTO();
        return AdminUserAuthenticationBO.builder()
                .userId(userAuthenticationDTO.getUserId())
                .tenantId(userAuthenticationDTO.getTenantId())
                .authorities(userAuthenticationDTO.getAuthorities())
                .scopes(userAuthenticationDTO.getScopes())
                .accessTokenBO(
                        AdminAccessTokenBO.builder()
                                .token(accessTokenDTO.getToken())
                                .expireTime(accessTokenDTO.getExpireTime())
                                .issuedTime(accessTokenDTO.getIssuedTime())
                                .tokenType(accessTokenDTO.getTokenType())
                                .expiresIn(accessTokenDTO.getExpiresIn())
                                .build()
                )
                .build();
    }
}
