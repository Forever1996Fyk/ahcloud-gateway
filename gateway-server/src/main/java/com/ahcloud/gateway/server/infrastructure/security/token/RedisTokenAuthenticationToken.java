package com.ahcloud.gateway.server.infrastructure.security.token;

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

    private final String token;

    private final String prefix;
    /**
     * Creates a token with the supplied array of authorities.
     *
     * @param token
     * @param prefix
     */
    public RedisTokenAuthenticationToken(String token, String prefix) {
        super(Collections.emptyList());
        this.token = token;
        this.prefix = prefix;
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
}
