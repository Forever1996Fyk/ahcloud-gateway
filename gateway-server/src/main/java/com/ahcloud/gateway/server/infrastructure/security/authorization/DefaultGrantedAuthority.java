package com.ahcloud.gateway.server.infrastructure.security.authorization;

import org.springframework.security.core.GrantedAuthority;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/1/20 18:52
 **/
public class DefaultGrantedAuthority implements GrantedAuthority {

    /**
     * 权限标识
     */
    private final String authority;

    public DefaultGrantedAuthority(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority;
    }

}
