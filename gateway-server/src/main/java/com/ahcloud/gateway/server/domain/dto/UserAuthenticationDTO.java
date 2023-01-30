package com.ahcloud.gateway.server.domain.dto;

import lombok.*;

import java.util.List;
import java.util.Set;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/1/20 18:13
 **/
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserAuthenticationDTO {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 权限集合
     */
    private Set<String> authorities;

    /**
     * token信息
     */
    private AccessTokenDTO accessTokenDTO;

    /**
     * scope权限范围
     */
    private Set<String> scopes;
}
