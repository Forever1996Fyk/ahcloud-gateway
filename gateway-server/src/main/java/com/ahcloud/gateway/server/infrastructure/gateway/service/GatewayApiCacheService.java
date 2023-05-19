package com.ahcloud.gateway.server.infrastructure.gateway.service;

import com.ahcloud.gateway.core.domain.api.bo.ApiRefreshPatternDTO;
import com.ahcloud.gateway.core.domain.api.dto.ApiDefinitionDTO;

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
     * @param apiDefinitionDTO
     */
    void cacheCurrentApi(ApiDefinitionDTO apiDefinitionDTO);

    /**
     * 移除当前api
     *
     * @param apiDefinitionDTO
     */
    void removeCurrentApi(ApiDefinitionDTO apiDefinitionDTO);

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
