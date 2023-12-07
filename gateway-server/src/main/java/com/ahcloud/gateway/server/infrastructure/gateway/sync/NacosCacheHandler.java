package com.ahcloud.gateway.server.infrastructure.gateway.sync;

import com.ahcloud.common.utils.CollectionUtils;
import com.ahcloud.common.utils.JsonUtils;
import com.ahcloud.gateway.client.constant.NacosPathConstants;
import com.ahcloud.gateway.core.domain.sync.ApiSyncData;
import com.ahcloud.gateway.core.domain.sync.RouteSyncData;
import com.ahcloud.gateway.server.infrastructure.gateway.sync.service.ApiSyncService;
import com.ahcloud.gateway.server.infrastructure.gateway.sync.service.RouteSyncService;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executor;

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

    protected final ApiSyncService apiSyncService;
    protected final RouteSyncService routeSyncService;

    protected final Environment environment;

    protected NacosCacheHandler(ConfigService configService, ApiSyncService apiSyncService, RouteSyncService routeSyncService, Environment environment) {
        this.environment = environment;
        this.configService = configService;
        this.apiSyncService = apiSyncService;
        this.routeSyncService = routeSyncService;
    }

    public ConfigService getConfigService() {
        return configService;
    }

    protected void updateRouteList(final String configInfo) {
        log.info("route configInfo: {}", configInfo);
        if (StringUtils.isBlank(configInfo)) {
            return;
        }
        if (StringUtils.equals("{}", configInfo)) {
            return;
        }
        List<RouteSyncData> routeSyncDataList = JsonUtils.jsonToList(configInfo, RouteSyncData.class);
        if (CollectionUtils.isEmpty(routeSyncDataList)) {
            return;
        }
        routeSyncService.handleRouteSyncData(routeSyncDataList);
    }

    protected void updateApiList(final String configInfo) {
        log.info("api configInfo: {}", configInfo);
        if (StringUtils.isBlank(configInfo)) {
            return;
        }
        if (StringUtils.equals("{}", configInfo)) {
            return;
        }
        List<ApiSyncData> apiSyncDataList = JsonUtils.jsonToList(configInfo, ApiSyncData.class);
        if (CollectionUtils.isEmpty(apiSyncDataList)) {
            return;
        }
        apiSyncService.handleApiSyncData(apiSyncDataList);
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
