package com.ahcloud.gateway.server.infrastructure.config;

import com.ahcloud.gateway.server.infrastructure.gateway.listener.LocalRouteDataChangeListener;
import com.ahcloud.gateway.server.infrastructure.gateway.sync.NacosSyncDataServiceImpl;
import com.ahcloud.gateway.server.infrastructure.gateway.sync.SyncDataService;
import com.ahcloud.gateway.server.infrastructure.gateway.sync.service.ApiSyncService;
import com.ahcloud.gateway.server.infrastructure.gateway.sync.service.RouteSyncService;
import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.nacos.api.config.ConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Objects;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/2/6 22:52
 **/
@Slf4j
@Configuration
@ConditionalOnClass
public class NacosSyncDataConfiguration {

    /**
     * nacos sync data service
     *
     * @param configManager the config service
     * @param routeSyncServices the gateway service
     *
     * @return the sync data service
     */
    @Bean
    public SyncDataService nacosSyncDataServiceImpl(final ObjectProvider<NacosConfigManager> configManager,
                                                    final ObjectProvider<ApiSyncService> apiSyncServices,
                                                    final ObjectProvider<RouteSyncService> routeSyncServices,
                                                    final ObjectProvider<Environment> environment) {
        log.info("you use nacos sync gateway data.......");
        return new NacosSyncDataServiceImpl(Objects.requireNonNull(configManager.getIfAvailable()).getConfigService(), apiSyncServices.getIfAvailable(), routeSyncServices.getIfAvailable(), environment.getIfAvailable());
    }

    @Bean
    public LocalRouteDataChangeListener localRouteDataChangeListener(RouteSyncService routeSyncService) {
        return new LocalRouteDataChangeListener(routeSyncService);
    }
}
