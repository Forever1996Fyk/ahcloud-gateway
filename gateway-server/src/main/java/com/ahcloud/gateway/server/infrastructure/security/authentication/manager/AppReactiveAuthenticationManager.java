package com.ahcloud.gateway.server.infrastructure.security.authentication.manager;

import com.ahcloud.gateway.client.enums.AppPlatformEnum;
import com.ahcloud.gateway.server.infrastructure.security.authentication.user.AppOAuth2User;
import com.ahcloud.gateway.server.infrastructure.security.token.authentication.bo.AccessTokenBO;
import com.ahcloud.gateway.server.infrastructure.security.token.authentication.bo.AppUserReactiveAuthenticationBO;
import com.ahcloud.gateway.server.infrastructure.security.token.service.TokenEndpointService;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Collection;
import java.util.Map;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/1/18 16:06
 **/
@Slf4j
@Component
public class AppReactiveAuthenticationManager extends AbstractReactiveAuthenticationManager {
    private final TokenEndpointService<AppUserReactiveAuthenticationBO> tokenEndpointService;

    @Autowired
    public AppReactiveAuthenticationManager(TokenEndpointService<AppUserReactiveAuthenticationBO> tokenEndpointService) {
        this.tokenEndpointService = tokenEndpointService;
    }

    @Override
    protected AppPlatformEnum getAppPlatform() {
        return AppPlatformEnum.APP;
    }

    @Override
    protected Triple<OAuth2AuthenticatedPrincipal, OAuth2AccessToken, Collection<? extends GrantedAuthority>> checkToken(Pair<String, String> tokenMark) {
        AppUserReactiveAuthenticationBO appUserReactiveAuthenticationBO = tokenEndpointService.authenticationByToken(tokenMark.getLeft(), tokenMark.getRight());
        Map<String, Object> attributes = Maps.newHashMap();
        attributes.put("userId", appUserReactiveAuthenticationBO.getUserId());
        attributes.put("tenantId", appUserReactiveAuthenticationBO.getTenantId());
        OAuth2User user = AppOAuth2User.builder()
                .userId(appUserReactiveAuthenticationBO.getUserId())
                .tenantId(appUserReactiveAuthenticationBO.getTenantId())
                .username(appUserReactiveAuthenticationBO.getAccount())
                .name(appUserReactiveAuthenticationBO.getUserName())
                .nickName(appUserReactiveAuthenticationBO.getNickName())
                .attributes(attributes)
                .build();

        AccessTokenBO accessTokenBO = appUserReactiveAuthenticationBO.getAccessTokenBO();
        OAuth2AccessToken accessToken = new OAuth2AccessToken(
                OAuth2AccessToken.TokenType.BEARER
                , accessTokenBO.getToken()
                , accessTokenBO.getIssuedAt()
                , accessTokenBO.getExpireAt()
                , appUserReactiveAuthenticationBO.getScopes()
        );
        return Triple.of(user, accessToken, user.getAuthorities());
    }
}
