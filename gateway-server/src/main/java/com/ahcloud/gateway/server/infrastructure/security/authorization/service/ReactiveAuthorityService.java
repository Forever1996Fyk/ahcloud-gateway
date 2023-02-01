package com.ahcloud.gateway.server.infrastructure.security.authorization.service;

import com.ahcloud.gateway.server.infrastructure.security.authorization.authority.UserReactiveAuthority;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/2/1 15:19
 **/
public interface ReactiveAuthorityService<T extends UserReactiveAuthority> {

    /**
     * 获取权限详情
     * @param userId
     * @param tenantId
     * @param path
     * @return
     */
    T getReactiveAuthority(Long userId, Long tenantId, String path);

    /**
     * 获取权限信息
     *
     * @param path
     * @return
     */
    T getReactiveAuthority(String path);

    /**
     * 权限校验
     * @param authority
     * @return
     */
    boolean verifyAuthority(T authority);

    /**
     * 权限校验
     * @param authority
     * @param authorities
     * @return
     */
    boolean verifyAuthority(T authority, Collection<? extends GrantedAuthority> authorities);

    /**
     * 校验流程
     * @param userId
     * @param tenantId
     * @param path
     * @param authorities
     * @return
     */
    boolean process(Long userId, Long tenantId, String path, Collection<? extends GrantedAuthority> authorities);
}
