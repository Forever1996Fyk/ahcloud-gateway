package com.ahcloud.gateway.server.helper;

import com.ahcloud.admin.client.domain.dubbo.token.dto.AccessTokenDTO;
import com.ahcloud.admin.client.domain.dubbo.token.response.AdminUserAuthenticationResponse;
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
     * @param response
     * @return
     */
    public static AdminUserAuthenticationBO convertBO(AdminUserAuthenticationResponse response) {
        AccessTokenDTO accessTokenDTO = response.getAccessTokenDTO();
        return AdminUserAuthenticationBO.builder()
                .userId(response.getUserId())
                .account(response.getAccount())
                .nickName(response.getNickName())
                .userName(response.getUserName())
                .tenantId(response.getTenantId())
                .authorities(response.getAuthorities())
                .scopes(response.getScopes())
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
