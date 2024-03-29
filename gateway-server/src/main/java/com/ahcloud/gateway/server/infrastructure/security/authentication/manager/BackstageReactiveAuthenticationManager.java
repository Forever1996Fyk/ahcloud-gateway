package com.ahcloud.gateway.server.infrastructure.security.authentication.manager;

import com.ahcloud.gateway.client.enums.AppPlatformEnum;
import com.ahcloud.gateway.server.infrastructure.security.authentication.user.BackstageOAuth2User;
import com.ahcloud.gateway.server.infrastructure.security.token.authentication.bo.AccessTokenBO;
import com.ahcloud.gateway.server.infrastructure.security.token.authentication.bo.BackstageUserReactiveAuthenticationBO;
import com.ahcloud.gateway.server.infrastructure.security.authorization.DefaultGrantedAuthority;
import com.ahcloud.gateway.server.infrastructure.security.token.service.TokenEndpointService;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

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
@Primary
@Component("backstageReactiveAuthenticationManager")
public class BackstageReactiveAuthenticationManager extends AbstractReactiveAuthenticationManager {

    private final TokenEndpointService<BackstageUserReactiveAuthenticationBO> tokenEndpointService;

    @Autowired
    public BackstageReactiveAuthenticationManager(TokenEndpointService<BackstageUserReactiveAuthenticationBO> adminTokenEndpointService) {
        this.tokenEndpointService = adminTokenEndpointService;
    }

    @Override
    protected AppPlatformEnum getAppPlatform() {
        return AppPlatformEnum.BACK_STAGE;
    }

    @Override
    protected Triple<OAuth2AuthenticatedPrincipal, OAuth2AccessToken, Collection<? extends GrantedAuthority>> checkToken(Pair<String, String> tokenMark) {
        BackstageUserReactiveAuthenticationBO backstageUserReactiveAuthenticationBO = tokenEndpointService.authenticationByToken(tokenMark.getLeft(), tokenMark.getRight());
        Map<String, Object> attributes = Maps.newHashMap();
        attributes.put("token", tokenMark.getLeft());
        OAuth2User user = BackstageOAuth2User.builder()
                .userId(backstageUserReactiveAuthenticationBO.getUserId())
                .tenantId(backstageUserReactiveAuthenticationBO.getTenantId())
                .username(backstageUserReactiveAuthenticationBO.getAccount())
                .authorities(convert(backstageUserReactiveAuthenticationBO.getAuthorities()))
                .attributes(attributes)
                .name(backstageUserReactiveAuthenticationBO.getUserName())
                .nickName(backstageUserReactiveAuthenticationBO.getNickName())
                .build();

        AccessTokenBO accessTokenBO = backstageUserReactiveAuthenticationBO.getAccessTokenBO();
        OAuth2AccessToken accessToken = new OAuth2AccessToken(
                OAuth2AccessToken.TokenType.BEARER
                , accessTokenBO.getToken()
                , accessTokenBO.getIssuedAt()
                , accessTokenBO.getExpireAt()
                , backstageUserReactiveAuthenticationBO.getScopes()
        );
        return Triple.of(user, accessToken, user.getAuthorities());
    }

    private Collection<? extends GrantedAuthority> convert(Set<String> authorities) {
        return authorities.stream()
                .map(DefaultGrantedAuthority::new)
                .collect(Collectors.toCollection(HashSet::new));
    }
}
