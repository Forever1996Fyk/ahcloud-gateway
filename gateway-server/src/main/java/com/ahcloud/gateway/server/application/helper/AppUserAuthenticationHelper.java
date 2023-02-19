package com.ahcloud.gateway.server.application.helper;

import com.ahcloud.gateway.server.domain.app.AppAccessTokenBO;
import com.ahcloud.gateway.server.domain.app.AppUserAuthenticationBO;
import com.ahcloud.uaa.client.domain.dubbo.token.AccessTokenDTO;
import com.ahcloud.uaa.client.domain.dubbo.token.AppUserAuthenticationDTO;

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
    public static AppUserAuthenticationBO convertBO(AppUserAuthenticationDTO userAuthenticationDTO) {
        AccessTokenDTO accessTokenDTO = userAuthenticationDTO.getAccessTokenDTO();
        return AppUserAuthenticationBO.builder()
                .userId(userAuthenticationDTO.getUserId())
                .tenantId(userAuthenticationDTO.getTenantId())
                .accessTokenBO(
                        AppAccessTokenBO.builder()
                                .token(accessTokenDTO.getToken())
                                .expireAt(accessTokenDTO.getExpireAt())
                                .issuedAt(accessTokenDTO.getIssuedAt())
                                .tokenType(accessTokenDTO.getTokenType())
                                .expiresIn(accessTokenDTO.getExpiresIn())
                                .build()
                )
                .build();
    }
}
