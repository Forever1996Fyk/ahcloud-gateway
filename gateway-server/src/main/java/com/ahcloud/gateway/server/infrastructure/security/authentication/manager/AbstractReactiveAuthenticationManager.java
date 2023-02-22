package com.ahcloud.gateway.server.infrastructure.security.authentication.manager;

import com.ahcloud.common.utils.CollectionUtils;
import com.ahcloud.gateway.client.enums.AppPlatformEnum;
import com.ahcloud.gateway.client.enums.GatewayRetCodeEnum;
import com.ahcloud.gateway.server.infrastructure.exception.GatewayAuthenticationException;
import com.ahcloud.gateway.server.infrastructure.security.token.RedisTokenAuthenticationToken;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.skywalking.apm.toolkit.trace.TraceContext;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Collection;
import java.util.Objects;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/1/17 22:23
 **/
@Slf4j
public abstract class AbstractReactiveAuthenticationManager implements ReactiveAuthenticationManager {

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        log.info("AbstractReactiveAuthenticationManager[authenticate] start user authentication by app_platform is {}", getAppPlatform());
        return Mono.justOrEmpty(authentication)
                .filter(RedisTokenAuthenticationToken.class::isInstance)
                .cast(RedisTokenAuthenticationToken.class)
                .map(RedisTokenAuthenticationToken::getTokenMark)
                .flatMap(this::authenticate);
    }

    private Mono<Authentication> authenticate(Pair<String, String> tokenMark) {
        /*
        校验token
            1、token是否过期(必须)
                1.1、token未过期, 则返回过期时间
                1.2、token已过期, 则抛出token过期异常
            2、token生成时间(必须)
            3、用户相关信息(必须)
                3.1、用户id
                3.2、用户权限(可选)
            4、权限列表(可选)
            5、scope权限范围列表(一般OAuth2 单点登录时,用于判断第三方是否有权限访问)
         */
        Triple<OAuth2AuthenticatedPrincipal, OAuth2AccessToken, Collection<? extends GrantedAuthority>> triple = checkToken(tokenMark);
        OAuth2AuthenticatedPrincipal principal = triple.getLeft();
        OAuth2AccessToken accessToken = triple.getMiddle();
        Collection<? extends GrantedAuthority> authorities = triple.getRight();
        validateTokenAuthentication(principal, accessToken, authorities);
        return Mono.just(new BearerTokenAuthentication(principal, accessToken, authorities));
    }

    /**
     * 认证参数校验
     * @param principal
     * @param token
     * @param authorities
     */
    protected void validateTokenAuthentication(OAuth2AuthenticatedPrincipal principal, OAuth2AccessToken token, Collection<? extends GrantedAuthority> authorities) {
        validateOAuth2Token(token);
        validatePrincipal(principal);
//        validateAuthority(authorities);
    }

    protected void validatePrincipal(OAuth2AuthenticatedPrincipal principal) {
        if (Objects.isNull(principal)) {
            throw new GatewayAuthenticationException(GatewayRetCodeEnum.AUTHENTICATION_USER_PRINCIPAL_ERROR);
        }
    }

    protected void validateOAuth2Token(OAuth2AccessToken token) {
        if (Objects.isNull(token)) {
            throw new GatewayAuthenticationException(GatewayRetCodeEnum.CERTIFICATE_EXCEPTION_ERROR);
        }
        Instant expiresAt = token.getExpiresAt();
        // token生成时间
        Instant issuedAt = token.getIssuedAt();
        if (Objects.nonNull(expiresAt) && expiresAt.isBefore(Instant.now())) {
            throw new GatewayAuthenticationException(GatewayRetCodeEnum.CERTIFICATE_EXPIRED_ERROR);
        }
        // 如果生成时间小于过期时间，则表示token异常
        if (Objects.nonNull(issuedAt) && Objects.nonNull(expiresAt) && issuedAt.isAfter(expiresAt)) {
            throw new GatewayAuthenticationException(GatewayRetCodeEnum.CERTIFICATE_EXCEPTION_ERROR);
        }
    }

    protected void validateAuthority(Collection<? extends GrantedAuthority> authorities) {
        if (CollectionUtils.isEmpty(authorities)) {
            throw new GatewayAuthenticationException(GatewayRetCodeEnum.AUTHENTICATION_AUTHORITY_ERROR);
        }
    }

    /**
     * AppPlatformEnum
     *
     * @return
     */
    protected abstract AppPlatformEnum getAppPlatform();

    /**
     * 校验token
     *
     * @param tokenMark left: token值; right: token前缀(缓存)
     * @return
     */
    protected abstract Triple<OAuth2AuthenticatedPrincipal, OAuth2AccessToken, Collection<? extends GrantedAuthority>> checkToken(Pair<String, String> tokenMark);
}
