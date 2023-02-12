package com.ahcloud.gateway.server.infrastructure.gateway.service.impl;

import com.ahcloud.gateway.server.infrastructure.constant.NacosPathConstants;
import com.ahcloud.gateway.server.infrastructure.gateway.service.GatewayService;
import com.ahcloud.gateway.server.infrastructure.gateway.service.NacosCacheHandler;
import com.ahcloud.gateway.server.infrastructure.gateway.service.SyncDataService;
import com.alibaba.nacos.api.config.ConfigService;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/2/7 08:55
 **/
public class NacosSyncDataServiceImpl extends NacosCacheHandler implements SyncDataService {


    public NacosSyncDataServiceImpl(ConfigService configService, GatewayService gatewayService) {
        super(configService, gatewayService);
        start();
    }

    public void start() {
        watcherData(NacosPathConstants.ROUTE_DATA_ID, this::updateRouteMap);
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
