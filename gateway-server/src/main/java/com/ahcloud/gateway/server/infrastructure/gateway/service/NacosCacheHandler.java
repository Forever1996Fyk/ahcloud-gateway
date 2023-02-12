package com.ahcloud.gateway.server.infrastructure.gateway.service;

import com.ahcloud.common.utils.CollectionUtils;
import com.ahcloud.common.utils.JsonUtils;
import com.ahcloud.gateway.server.infrastructure.constant.NacosPathConstants;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.RouteDefinition;

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

    protected NacosCacheHandler(ConfigService configService, GatewayService gatewayService) {
        this.configService = configService;
        this.gatewayService = gatewayService;
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
        List<RouteDefinition> routeDefinitions = values.stream().map(
                value -> JsonUtils.mapToBean(value, RouteDefinition.class)
        ).collect(Collectors.toList());
        gatewayService.refreshRoute(routeDefinitions).subscribe();
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
