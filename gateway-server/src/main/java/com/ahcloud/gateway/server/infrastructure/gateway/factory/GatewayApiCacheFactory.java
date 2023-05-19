package com.ahcloud.gateway.server.infrastructure.gateway.factory;

import com.ahcloud.common.utils.CollectionUtils;
import com.ahcloud.gateway.core.domain.api.bo.ApiRefreshPatternDTO;
import com.ahcloud.gateway.core.domain.api.dto.ApiDefinitionDTO;
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

    public static void refreshCacheApi(List<ApiDefinitionDTO> apiDefinitionDTOList) {
        if (CollectionUtils.isEmpty(apiDefinitionDTOList)) {
            return;
        }
        for (ApiDefinitionDTO apiDefinitionDTO : apiDefinitionDTOList) {
            LOCAL_CACHE_SERVICE.removeCurrentApi(apiDefinitionDTO);
            LOCAL_CACHE_SERVICE.cacheCurrentApi(apiDefinitionDTO);
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
