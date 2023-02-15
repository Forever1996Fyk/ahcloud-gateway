package com.ahcloud.gateway.server.application.helper;

import com.ahcloud.admin.client.domain.dubbo.token.AccessTokenDTO;
import com.ahcloud.admin.client.domain.dubbo.token.AdminUserAuthenticationDTO;
import com.ahcloud.gateway.server.domain.admin.bo.AdminAccessTokenBO;
import com.ahcloud.gateway.server.domain.admin.bo.AdminUserAuthenticationBO;
import com.ahcloud.gateway.server.domain.app.AppAccessTokenBO;
import com.ahcloud.gateway.server.domain.app.AppUserAuthenticationBO;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/1/31 17:15
 **/
public class AppUserAuthenticationHelper {

    /**
     * 数据转换
     * @param userAuthenticationDTO
     * @return
     */
    public static AppUserAuthenticationBO convertBO(AdminUserAuthenticationDTO userAuthenticationDTO) {
        AccessTokenDTO accessTokenDTO = userAuthenticationDTO.getAccessTokenDTO();
        return AppUserAuthenticationBO.builder()
                .userId(userAuthenticationDTO.getUserId())
                .scopes(userAuthenticationDTO.getScopes())
                .accessTokenBO(
                        AppAccessTokenBO.builder()
                                .token(accessTokenDTO.getToken())
                                .expireAt(accessTokenDTO.getExpireAt())
                                .issuedAt(accessTokenDTO.getIssuedAt())
                                .tokenType(accessTokenDTO.getTokenType())
                                .build()
                )
                .build();
    }
}
