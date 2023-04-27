package com.ahcloud.gateway.server.infrastructure.gateway.factory;

import com.ahcloud.common.utils.CollectionUtils;
import com.ahcloud.gateway.core.domain.api.bo.ApiRefreshPatternDTO;
import com.ahcloud.gateway.core.domain.api.dto.ApiRefreshDTO;
import com.ahcloud.gateway.server.infrastructure.gateway.service.GatewayApiCacheService;
import com.ahcloud.gateway.server.infrastructure.gateway.service.impl.GatewayApiCacheServiceImpl;
import com.google.common.collect.ImmutableSet;

import java.util.List;
import java.util.Set;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/2/20 21:12
 **/
public class GatewayApiCacheFactory {

    private final static GatewayApiCacheService LOCAL_CACHE_SERVICE = new GatewayApiCacheServiceImpl();

    public static void refreshCacheApi(List<ApiRefreshDTO> apiRefreshDTOList) {
        if (CollectionUtils.isEmpty(apiRefreshDTOList)) {
            return;
        }
        for (ApiRefreshDTO apiRefreshDTO : apiRefreshDTOList) {
            LOCAL_CACHE_SERVICE.removeCurrentApi(apiRefreshDTO);
            LOCAL_CACHE_SERVICE.cacheCurrentApi(apiRefreshDTO);
        }
    }

    /**
     * 从本地缓存获取
     *
     * @return ImmutableSet
     */
    public static Set<ApiRefreshPatternDTO> getValues() {
        return ImmutableSet.copyOf(LOCAL_CACHE_SERVICE.getValues());
    }

}
