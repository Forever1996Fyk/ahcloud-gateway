package com.ahcloud.gateway.server.infrastructure.security.authentication.manager;

import com.ahcloud.gateway.client.AppPlatformEnum;
import com.ahcloud.gateway.server.domain.dto.AccessTokenDTO;
import com.ahcloud.gateway.server.domain.dto.UserAuthenticationDTO;
import com.ahcloud.gateway.server.infrastructure.security.authorization.DefaultGrantedAuthority;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.*;
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

    @Override
    protected AppPlatformEnum getAppPlatform() {
        return AppPlatformEnum.SYSTEM_WEB;
    }

    @Override
    protected Triple<OAuth2AuthenticatedPrincipal, OAuth2AccessToken, Collection<? extends GrantedAuthority>> checkToken(Pair<String, String> tokenMark) {
        // 根据token获取用户认证信息, 先从缓存获取，再从dubbo调用接口获取 todo
        UserAuthenticationDTO userAuthenticationDTO = UserAuthenticationDTO.builder().build();
        Map<String, Object> attributes = Maps.newHashMap();
        attributes.put("userId", userAuthenticationDTO.getUserId());
        OAuth2User user = new DefaultOAuth2User(convert(userAuthenticationDTO.getAuthorities()), attributes, "userId");

        AccessTokenDTO accessTokenDTO = userAuthenticationDTO.getAccessTokenDTO();
        OAuth2AccessToken accessToken = new OAuth2AccessToken(
                OAuth2AccessToken.TokenType.BEARER
                , accessTokenDTO.getToken()
                , Instant.ofEpochSecond(accessTokenDTO.getIssuedTime())
                , Instant.ofEpochSecond(accessTokenDTO.getExpireTime())
                , userAuthenticationDTO.getScopes()
        );
        return Triple.of(user, accessToken, user.getAuthorities());
    }

    private Collection<? extends GrantedAuthority> convert(Set<String> authorities) {
        return authorities.stream()
                .map(DefaultGrantedAuthority::new)
                .collect(Collectors.toCollection(HashSet::new));
    }
}
