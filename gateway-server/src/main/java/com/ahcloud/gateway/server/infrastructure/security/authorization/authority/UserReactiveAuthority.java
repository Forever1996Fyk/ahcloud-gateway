package com.ahcloud.gateway.server.infrastructure.security.authorization.authority;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/2/1 14:47
 **/
public interface UserReactiveAuthority {

    /**
     * 用户id
     * @return
     */
    Long getUserId();

    /**
     * 租户id
     * @return
     */
    Long getTenantId();

    /**
     * 权限标识
     * @return
     */
    String getAuthority();

    /**
     * 请求路径
     * @return
     */
    String getPath();

    /**
     * 是否可用
     *
     * @return
     */
    boolean isEnabled();

    /**
     * 是否需要认证
     *
     * @return
     */
    boolean isAuth();
}
