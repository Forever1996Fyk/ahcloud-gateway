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
