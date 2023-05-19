package com.ahcloud.gateway.server.infrastructure.gateway.service;

import com.ahcloud.common.utils.CollectionUtils;
import com.ahcloud.common.utils.JsonUtils;
import com.ahcloud.gateway.core.domain.api.dto.ApiDefinitionDTO;
import com.ahcloud.gateway.core.domain.route.dto.RouteDefinitionDTO;
import com.ahcloud.gateway.core.infrastructure.constant.NacosPathConstants;
import com.ahcloud.gateway.server.helper.RouteDefinitionHelper;
import com.ahcloud.gateway.server.infrastructure.gateway.factory.GatewayApiCacheFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;

import java.util.*;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/2/7 08:57
 **/
@Slf4j
public abstract class NacosCacheHandler {

    protected static final Map<String, List<Listener>> LISTENERS = Maps.newConcurrentMap();

    protected final ConfigService configService;

    protected final GatewayService gatewayService;

    protected final Environment environment;

    protected NacosCacheHandler(ConfigService configService, GatewayService gatewayService, Environment environment) {
        this.configService = configService;
        this.gatewayService = gatewayService;
        this.environment = environment;
    }

    public ConfigService getConfigService() {
        return configService;
    }

    protected void updateRouteMap(final String configInfo) {
        log.info("configInfo: {}", configInfo);
        Map<String, Map<String, Object>> routeDefinitionMap = JsonUtils.stringToMap2(configInfo);
        if (CollectionUtils.isEmpty(routeDefinitionMap) && CollectionUtils.isNotEmpty(routeDefinitionMap.values())) {
            return;
        }
        Collection<Map<String, Object>> values = routeDefinitionMap.values();
        List<RouteDefinitionDTO> routeDefinitionDTOList = values.stream().map(
                value -> JsonUtils.mapToBean(value, RouteDefinitionDTO.class)
        ).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(routeDefinitionDTOList)) {
            gatewayService.refreshRoute(RouteDefinitionHelper.convertToList(routeDefinitionDTOList)).subscribe();
        }
    }

    protected void updateApiRefreshMap(final String configInfo) {
        log.info("configInfo: {}", configInfo);
        Map<String, Map<String, Object>> apiRefreshMap = JsonUtils.stringToMap2(configInfo);
        if (CollectionUtils.isEmpty(apiRefreshMap) && CollectionUtils.isNotEmpty(apiRefreshMap.values())) {
            return;
        }
        Collection<Map<String, Object>> values = apiRefreshMap.values();
        List<ApiDefinitionDTO> apiDefinitionDTOList = values.stream().map(
                value -> JsonUtils.mapToBean(value, ApiDefinitionDTO.class)
        ).collect(Collectors.toList());
        GatewayApiCacheFactory.refreshCacheApi(apiDefinitionDTOList);
    }

    protected void watcherData(final String dataId, final OnChange oc) {
        Listener listener = new Listener() {
            @Override
            public Executor getExecutor() {
                return null;
            }

            @Override
            public void receiveConfigInfo(String configInfo) {
                oc.change(configInfo);
            }
        };
        oc.change(getConfigAndSignListener(dataId, listener));
        LISTENERS.computeIfAbsent(dataId, key -> new ArrayList<>()).add(listener);
    }

    private String getConfigAndSignListener(final String dataId, final Listener listener) {
        String config = null;
        try {
            config = configService.getConfigAndSignListener(dataId, NacosPathConstants.GROUP, 6000, listener);
        } catch (NacosException e) {
            log.error(e.getMessage(), e);
        }
        if (Objects.isNull(config)) {
            config = "{}";
        }
        return config;
    }

    protected interface OnChange {
        /**
         * 数据变更
         * @param changeData
         */
        void  change(String changeData);
    }
}
