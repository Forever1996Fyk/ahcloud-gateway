package com.ahcloud.gateway.server.infrastructure.security.authentication.manager;

import com.ahcloud.gateway.client.enums.AppPlatformEnum;
import com.ahcloud.gateway.server.infrastructure.security.authentication.user.AdminOAuth2User;
import com.ahcloud.gateway.server.infrastructure.security.token.authentication.bo.AccessTokenBO;
import com.ahcloud.gateway.server.infrastructure.security.token.authentication.bo.AdminUserReactiveAuthenticationBO;
import com.ahcloud.gateway.server.infrastructure.security.authorization.DefaultGrantedAuthority;
import com.ahcloud.gateway.server.infrastructure.security.token.service.TokenEndpointService;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/1/18 16:06
 **/
@Slf4j
@Component
public class SystemWebReactiveAuthenticationManager extends AbstractReactiveAuthenticationManager {

    private final TokenEndpointService<AdminUserReactiveAuthenticationBO> tokenEndpointService;

    @Autowired
    public SystemWebReactiveAuthenticationManager(TokenEndpointService<AdminUserReactiveAuthenticationBO> adminTokenEndpointService) {
        this.tokenEndpointService = adminTokenEndpointService;
    }

    @Override
    protected AppPlatformEnum getAppPlatform() {
        return AppPlatformEnum.SYSTEM_WEB;
    }

    @Override
    protected Triple<OAuth2AuthenticatedPrincipal, OAuth2AccessToken, Collection<? extends GrantedAuthority>> checkToken(Pair<String, String> tokenMark) {
        AdminUserReactiveAuthenticationBO adminUserAuthenticationBO = tokenEndpointService.authenticationByToken(tokenMark.getLeft(), tokenMark.getRight());
        Map<String, Object> attributes = Maps.newHashMap();
        attributes.put("userId", adminUserAuthenticationBO.getUserId());
        attributes.put("tenantId", adminUserAuthenticationBO.getTenantId());
        OAuth2User user = AdminOAuth2User.builder()
                .userId(adminUserAuthenticationBO.getUserId())
                .tenantId(adminUserAuthenticationBO.getTenantId())
                .username(adminUserAuthenticationBO.getAccount())
                .authorities(convert(adminUserAuthenticationBO.getAuthorities()))
                .build();

        AccessTokenBO accessTokenBO = adminUserAuthenticationBO.getAccessTokenBO();
        OAuth2AccessToken accessToken = new OAuth2AccessToken(
                OAuth2AccessToken.TokenType.BEARER
                , accessTokenBO.getToken()
                , Instant.ofEpochSecond(accessTokenBO.getIssuedTime())
                , Instant.ofEpochSecond(accessTokenBO.getExpireTime())
                , adminUserAuthenticationBO.getScopes()
        );
        return Triple.of(user, accessToken, user.getAuthorities());
    }

    private Collection<? extends GrantedAuthority> convert(Set<String> authorities) {
        return authorities.stream()
                .map(DefaultGrantedAuthority::new)
                .collect(Collectors.toCollection(HashSet::new));
    }
}
