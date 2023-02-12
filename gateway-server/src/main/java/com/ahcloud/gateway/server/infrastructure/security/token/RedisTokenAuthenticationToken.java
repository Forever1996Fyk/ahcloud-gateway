package com.ahcloud.gateway.server.infrastructure.security.token;

import com.ahcloud.gateway.client.enums.AppPlatformEnum;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.Collections;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/1/18 10:52
 **/
public class RedisTokenAuthenticationToken extends AbstractAuthenticationToken {

    private static final long serialVersionUID = 1532870852441248905L;
    private final String token;

    private final String prefix;
    
    private final AppPlatformEnum appPlatformEnum;
    /**
     * Creates a token with the supplied array of authorities.
     *
     * @param token
     * @param prefix
     * @param appPlatformEnum
     */
    public RedisTokenAuthenticationToken(String token, String prefix, AppPlatformEnum appPlatformEnum) {
        super(Collections.emptyList());
        this.token = token;
        this.prefix = prefix;
        this.appPlatformEnum = appPlatformEnum;
    }

    @Override
    public Object getCredentials() {
        return this.getToken();
    }

    @Override
    public Object getPrincipal() {
        return this.getToken();
    }

    public String getToken() {
        return token;
    }

    public String getPrefix() {
        return prefix;
    }

    public ImmutablePair<String, String> getTokenMark() {
        return ImmutablePair.of(getToken(), getPrefix());
    }

    public AppPlatformEnum getAppPlatformEnum() {
        return appPlatformEnum;
    }
}
