package com.ahcloud.gateway.server.domain.admin.bo;

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
public class AdminUserAuthenticationBO {

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
    private String username;

    /**
     * scope权限范围
     */
    private Set<String> scopes;

    /**
     * 权限集合
     */
    private Set<String> authorities;

    /**
     * token信息
     */
    private AdminAccessTokenBO accessTokenBO;
}
