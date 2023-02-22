package com.ahcloud.gateway.server.infrastructure.gateway.service.impl;

import com.ahcloud.gateway.core.infrastructure.constant.NacosPathConstants;
import com.ahcloud.gateway.server.infrastructure.gateway.service.GatewayService;
import com.ahcloud.gateway.server.infrastructure.gateway.service.NacosCacheHandler;
import com.ahcloud.gateway.server.infrastructure.gateway.service.SyncDataService;
import com.alibaba.nacos.api.config.ConfigService;
import org.springframework.core.env.Environment;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/2/7 08:55
 **/
public class NacosSyncDataServiceImpl extends NacosCacheHandler implements SyncDataService {


    public NacosSyncDataServiceImpl(ConfigService configService, GatewayService gatewayService, Environment environment) {
        super(configService, gatewayService, environment);
        start();
    }

    public void start() {
        watcherData(NacosPathConstants.ROUTE_DATA_ID, this::updateRouteMap);
        watcherData(NacosPathConstants.API_DATA_ID, this::updateApiRefreshMap);
    }


    @Override
    public void close() throws Exception {
        LISTENERS.forEach((dataId, lss) -> {
            lss.forEach(listener -> getConfigService().removeListener(dataId, NacosPathConstants.GROUP, listener));
            lss.clear();
        });
        LISTENERS.clear();
    }
}
