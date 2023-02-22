package com.ahcloud.gateway.server.infrastructure.config;

import com.ahcloud.gateway.core.infrastructure.gateway.listener.DataChangeListener;
import com.ahcloud.gateway.core.infrastructure.gateway.listener.nacos.NacosDataChangeListener;
import com.ahcloud.gateway.server.infrastructure.gateway.service.GatewayService;
import com.ahcloud.gateway.server.infrastructure.gateway.service.SyncDataService;
import com.ahcloud.gateway.server.infrastructure.gateway.service.impl.NacosSyncDataServiceImpl;
import com.alibaba.cloud.nacos.NacosConfigManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
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
     * @param gatewayService the gateway service
     *
     * @return the sync data service
     */
    @Bean
    public SyncDataService nacosSyncDataServiceImpl(final ObjectProvider<NacosConfigManager> configManager, final ObjectProvider<GatewayService> gatewayService, final ObjectProvider<Environment> environment) {
        log.info("you use nacos sync gateway data.......");
        return new NacosSyncDataServiceImpl(Objects.requireNonNull(configManager.getIfAvailable()).getConfigService(), gatewayService.getIfAvailable(), environment.getIfAvailable());
    }
}
