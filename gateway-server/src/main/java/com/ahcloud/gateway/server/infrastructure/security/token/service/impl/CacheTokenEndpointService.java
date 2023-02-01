package com.ahcloud.gateway.server.infrastructure.security.token.service.impl;

import com.ahcloud.gateway.client.enums.GatewayRetCodeEnum;
import com.ahcloud.gateway.server.infrastructure.security.token.authentication.UserReactiveAuthentication;
import com.ahcloud.gateway.server.infrastructure.constant.CacheKey;
import com.ahcloud.gateway.server.infrastructure.exception.GatewayAuthenticationException;
import com.ahcloud.gateway.server.infrastructure.security.token.service.TokenEndpointService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Objects;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/2/1 09:09
 **/
@Slf4j
public abstract class CacheTokenEndpointService<T extends UserReactiveAuthentication> implements TokenEndpointService<T> {

    private final RedisTemplate<String, Object> redisTemplate;

    protected CacheTokenEndpointService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    @Override
    public T authenticationByToken(String token, String prefix) {
        String cacheKey = CacheKey.generateAuthenticationCacheKey(prefix, token);
        T t = (T) redisTemplate.opsForValue().get(cacheKey);
        if (Objects.isNull(t)) {
            t = createUserReactiveAuthentication(token);
            if (Objects.isNull(t)) {
                log.error("{}[authenticationByToken] 认证失败， 用户认证信息不存在, token is {}, prefix is {}", getLogMark(), token, prefix);
                throw new GatewayAuthenticationException(GatewayRetCodeEnum.GATEWAY_USER_AUTHENTICATION_FAILED);
            }
            redisTemplate.opsForValue().set(cacheKey, t);
        }
        return t;
    }

    /**
     * 创建用户新的认证信息
     *
     * @param token
     * @return
     */
    protected abstract T createUserReactiveAuthentication(String token);

    /**
     * 日志标识
     * @return
     */
    protected abstract String getLogMark();
}
