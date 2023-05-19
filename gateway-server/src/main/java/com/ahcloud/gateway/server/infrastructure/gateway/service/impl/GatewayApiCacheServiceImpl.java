package com.ahcloud.gateway.server.infrastructure.gateway.service.impl;

import com.ahcloud.gateway.core.domain.api.bo.ApiRefreshPatternDTO;
import com.ahcloud.gateway.core.domain.api.dto.ApiDefinitionDTO;
import com.ahcloud.gateway.server.infrastructure.gateway.service.GatewayApiCacheService;
import com.google.common.collect.Maps;
import org.springframework.web.util.pattern.PathPatternParser;

import java.util.Collection;
import java.util.Map;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/2/20 21:08
 **/
public class GatewayApiCacheServiceImpl implements GatewayApiCacheService {
    private final static PathPatternParser DEFAULT_PATH_PARSER = new PathPatternParser();

    private final static Map<String, ApiRefreshPatternDTO> API_PATTERN_CACHE = Maps.newConcurrentMap();

    @Override
    public void cacheCurrentApi(ApiDefinitionDTO apiDefinitionDTO) {
        API_PATTERN_CACHE.put(apiDefinitionDTO.getPath(),
                new ApiRefreshPatternDTO(DEFAULT_PATH_PARSER.parse(apiDefinitionDTO.getPath()), apiDefinitionDTO)
        );
    }

    @Override
    public void removeCurrentApi(ApiDefinitionDTO apiDefinitionDTO) {
        API_PATTERN_CACHE.remove(apiDefinitionDTO.getPath());
    }

    @Override
    public Collection<ApiRefreshPatternDTO> getValues() {
        return API_PATTERN_CACHE.values();
    }

    @Override
    public void clearAll() {
        API_PATTERN_CACHE.clear();
    }
}
