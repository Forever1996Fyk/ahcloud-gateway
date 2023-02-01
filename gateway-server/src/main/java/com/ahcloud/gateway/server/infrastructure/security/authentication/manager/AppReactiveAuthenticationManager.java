package com.ahcloud.gateway.server.infrastructure.security.authentication.manager;

import com.ahcloud.gateway.client.enums.AppPlatformEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/1/18 16:06
 **/
@Slf4j
@Component
public class AppReactiveAuthenticationManager extends AbstractReactiveAuthenticationManager {

    public AppReactiveAuthenticationManager() {
        super(tokenEndpointService);
    }

    @Override
    protected AppPlatformEnum getAppPlatform() {
        return AppPlatformEnum.APP;
    }

    @Override
    protected Triple<OAuth2AuthenticatedPrincipal, OAuth2AccessToken, Collection<? extends GrantedAuthority>> checkToken(Pair<String, String> tokenMark) {
        return null;
    }
}
