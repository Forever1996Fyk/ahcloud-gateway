package com.ahcloud.gateway.server.infrastructure.gateway.service.impl;

import com.ahcloud.gateway.client.enums.ApiStatusEnum;
import com.ahcloud.gateway.core.domain.api.bo.ApiRefreshPatternBO;
import com.ahcloud.gateway.core.domain.api.dto.ApiRefreshDTO;
import com.ahcloud.gateway.server.infrastructure.gateway.service.GatewayApiCacheService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.units.qual.C;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/2/20 21:08
 **/
public class GatewayApiCacheServiceImpl implements GatewayApiCacheService {
    private final static PathPatternParser DEFAULT_PATH_PARSER = new PathPatternParser();

    private final static Map<String, ApiRefreshPatternBO> API_PATTERN_CACHE = Maps.newConcurrentMap();

    @Override
    public void cacheCurrentApi(ApiRefreshDTO apiRefreshDTO) {
        API_PATTERN_CACHE.put(apiRefreshDTO.getPath(),
                new ApiRefreshPatternBO(DEFAULT_PATH_PARSER.parse(apiRefreshDTO.getPath()), apiRefreshDTO)
        );
    }

    @Override
    public void removeCurrentApi(ApiRefreshDTO apiRefreshDTO) {
        API_PATTERN_CACHE.remove(apiRefreshDTO.getPath());
    }

    @Override
    public Collection<ApiRefreshPatternBO> getValues() {
        return API_PATTERN_CACHE.values();
    }

    @Override
    public void clearAll() {
        API_PATTERN_CACHE.clear();
    }
}
