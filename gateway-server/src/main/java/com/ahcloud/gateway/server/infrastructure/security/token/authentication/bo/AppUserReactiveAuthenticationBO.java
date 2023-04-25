package com.ahcloud.gateway.server.infrastructure.security.token.authentication.bo;

import com.ahcloud.gateway.server.infrastructure.security.token.authentication.UserReactiveAuthentication;
import lombok.*;

import java.io.Serializable;
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
public class AppUserReactiveAuthenticationBO implements Serializable, UserReactiveAuthentication {
    private static final long serialVersionUID = 9218659322040814886L;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 租户id
     */
    private Long tenantId;

    /**
     * 用户账号
     */
    private String account;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 权限集合
     */
    private Set<String> authorities;

    /**
     * token信息
     */
    private AccessTokenBO accessTokenBO;

    /**
     * scope权限范围
     */
    private Set<String> scopes;
}
