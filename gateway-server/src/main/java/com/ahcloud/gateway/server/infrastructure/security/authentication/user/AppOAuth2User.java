package com.ahcloud.gateway.server.infrastructure.security.authentication.user;

import com.ahcloud.common.utils.CollectionUtils;
import com.google.common.collect.Maps;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/2/1 23:28
 **/
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AppOAuth2User implements OAuth2User, Serializable {
    private static final long serialVersionUID = 7705746145465853308L;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 租户id
     */
    private Long tenantId;

    /**
     * 可以做用户账号
     */
    private String username;

    /**
     * 参数属性
     */
    private Map<String, Object> attributes;

    @Override
    public Map<String, Object> getAttributes() {
        Map<String, Object> attributes = CollectionUtils.isEmpty(this.attributes) ? Maps.newHashMap() : this.attributes;
        attributes.put("userId", this.getUserId());
        attributes.put("tenantId", this.getTenantId());
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getName() {
        return this.username;
    }


}
