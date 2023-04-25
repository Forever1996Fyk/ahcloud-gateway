package com.ahcloud.gateway.server.domain.app;

import lombok.*;

import java.util.Set;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/1/31 16:30
 **/
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AppUserAuthenticationBO {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 租户id
     */
    private String tenantId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 账号
     */
    private String account;

    /**
     * scope权限范围
     */
    private Set<String> scopes;

    /**
     * token信息
     */
    private AppAccessTokenBO accessTokenBO;
}
