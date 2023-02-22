package com.ahcloud.gateway.server.infrastructure.gateway.service;

import com.ahcloud.gateway.core.domain.api.bo.ApiRefreshPatternBO;
import com.ahcloud.gateway.core.domain.api.dto.ApiRefreshDTO;
import org.checkerframework.checker.units.qual.A;

import java.util.Collection;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/2/20 21:07
 **/
public interface GatewayApiCacheService {

    /**
     * 缓存当前api
     *
     * @param apiRefreshDTO
     */
    void cacheCurrentApi(ApiRefreshDTO apiRefreshDTO);

    /**
     * 移除当前api
     *
     * @param apiRefreshDTO
     */
    void removeCurrentApi(ApiRefreshDTO apiRefreshDTO);

    /**
     * 获取所有缓存value
     * @return
     */
    Collection<ApiRefreshPatternBO> getValues();

    /**
     * 清除所有缓存
     */
    void clearAll();
}
