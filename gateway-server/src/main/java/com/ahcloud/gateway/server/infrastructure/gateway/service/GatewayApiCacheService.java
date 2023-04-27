package com.ahcloud.gateway.server.infrastructure.gateway.service;

import com.ahcloud.gateway.core.domain.api.bo.ApiRefreshPatternDTO;
import com.ahcloud.gateway.core.domain.api.dto.ApiRefreshDTO;

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
    Collection<ApiRefreshPatternDTO> getValues();

    /**
     * 清除所有缓存
     */
    void clearAll();
}
