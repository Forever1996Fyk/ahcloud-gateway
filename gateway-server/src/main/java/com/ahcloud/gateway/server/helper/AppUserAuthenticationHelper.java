package com.ahcloud.gateway.server.helper;

import com.ahcloud.gateway.server.domain.app.AppAccessTokenBO;
import com.ahcloud.gateway.server.domain.app.AppUserAuthenticationBO;

/**
 * @program: ahcloud-gateway
 * @description: 自定义实现
 * @author: YuKai Fan
 * @create: 2023/1/31 17:15
 **/
public class AppUserAuthenticationHelper {

//    /**
//     * 数据转换
//     * @param userAuthenticationDTO
//     * @return
//     */
//    public static AppUserAuthenticationBO convertBO(AppUserAuthenticationDTO userAuthenticationDTO) {
//        AccessTokenDTO accessTokenDTO = userAuthenticationDTO.getAccessTokenDTO();
//        return AppUserAuthenticationBO.builder()
//                .userId(userAuthenticationDTO.getUserId())
//                .userName(userAuthenticationDTO.getUserName())
//                .account(userAuthenticationDTO.getAccount())
//                .nickName(userAuthenticationDTO.getNickName())
//                .tenantId(userAuthenticationDTO.getTenantId())
//                .accessTokenBO(
//                        AppAccessTokenBO.builder()
//                                .token(accessTokenDTO.getToken())
//                                .expireAt(accessTokenDTO.getExpireAt())
//                                .issuedAt(accessTokenDTO.getIssuedAt())
//                                .tokenType(accessTokenDTO.getTokenType())
//                                .expiresIn(accessTokenDTO.getExpiresIn())
//                                .build()
//                )
//                .build();
//    }
}
