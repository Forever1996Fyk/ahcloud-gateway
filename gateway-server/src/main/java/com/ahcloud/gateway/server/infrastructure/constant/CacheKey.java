package com.ahcloud.gateway.server.infrastructure.constant;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/2/1 09:24
 **/
public class CacheKey {

    /**
     * 生成认证缓存key
     * @param prefix
     * @param token
     * @return
     */
    public static String generateAuthenticationCacheKey(String prefix, String token) {
        return prefix + ":" + token;
    }
}
