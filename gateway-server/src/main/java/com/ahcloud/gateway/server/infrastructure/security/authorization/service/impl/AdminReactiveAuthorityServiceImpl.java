package com.ahcloud.gateway.server.infrastructure.security.authorization.service.impl;

import com.ahcloud.gateway.client.enums.GatewayRetCodeEnum;
import com.ahcloud.gateway.server.domain.admin.bo.AdminAuthorityBO;
import com.ahcloud.gateway.server.infrastructure.exception.GatewayAccessDeniedException;
import com.ahcloud.gateway.server.infrastructure.security.authorization.authority.bo.AdminUserReactiveAuthorityBo;
import com.ahcloud.gateway.server.infrastructure.security.authorization.service.ReactiveAuthorityCacheService;
import com.ahcloud.gateway.server.infrastructure.security.authorization.service.ReactiveAuthorityService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collection;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/2/1 15:46
 **/
@Slf4j
@Component(value = "adminReactiveAuthorityService")
public class AdminReactiveAuthorityServiceImpl implements ReactiveAuthorityService<AdminUserReactiveAuthorityBo> {
    @Resource
    private ReactiveAuthorityCacheService authorityCacheService;

    @Override
    public AdminUserReactiveAuthorityBo getReactiveAuthority(Long userId, Long tenantId, String path) {
        AdminAuthorityBO authorityBO = authorityCacheService.getAdminAuthorityFromCacheByPath(path);
        return AdminUserReactiveAuthorityBo.builder()
                .enabled(authorityBO.getEnabled())
                .auth(authorityBO.getAuth())
                .authority(authorityBO.getAuthority())
                .userId(userId)
                .tenantId(tenantId)
                .path(authorityBO.getPath())
                .readOrWriteEnum(authorityBO.getReadOrWriteEnum())
                .build();
    }

    @Override
    public AdminUserReactiveAuthorityBo getReactiveAuthority(String path) {
        return this.getReactiveAuthority(null, null, path);
    }

    @Override
    public boolean verifyAuthority(AdminUserReactiveAuthorityBo authority) {
        if (!authority.isEnabled()) {
            throw new GatewayAccessDeniedException(GatewayRetCodeEnum.AUTHORIZATION_DISABLED);
        }
        if (StringUtils.isBlank(authority.getAuthority())) {
            throw new GatewayAccessDeniedException(GatewayRetCodeEnum.AUTHORIZATION_MARK_ERROR);
        }
        return true;
    }

    @Override
    public boolean verifyAuthority(AdminUserReactiveAuthorityBo authority, Collection<? extends GrantedAuthority> authorities) {
        this.verifyAuthority(authority);
        return authorities.stream()
                .anyMatch(grantedAuthority -> StringUtils.equals(authority.getAuthority(), grantedAuthority.getAuthority()));
    }

    @Override
    public boolean process(Long userId, Long tenantId, String path, Collection<? extends GrantedAuthority> authorities) {
        AdminUserReactiveAuthorityBo reactiveAuthority = this.getReactiveAuthority(userId, tenantId, path);
        return this.verifyAuthority(reactiveAuthority, authorities);
    }
}
