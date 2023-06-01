package com.ahcloud.gateway.server.infrastructure.gateway.sync;

import com.ahcloud.gateway.client.constant.NacosPathConstants;
import com.ahcloud.gateway.server.infrastructure.gateway.sync.NacosCacheHandler;
import com.ahcloud.gateway.server.infrastructure.gateway.sync.SyncDataService;
import com.ahcloud.gateway.server.infrastructure.gateway.sync.service.ApiSyncService;
import com.ahcloud.gateway.server.infrastructure.gateway.sync.service.RouteSyncService;
import com.alibaba.nacos.api.config.ConfigService;
import org.springframework.core.env.Environment;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/2/7 08:55
 **/
public class NacosSyncDataServiceImpl extends NacosCacheHandler implements SyncDataService {


    public NacosSyncDataServiceImpl(ConfigService configService, ApiSyncService apiSyncService, RouteSyncService routeSyncService, Environment environment) {
        super(configService, apiSyncService, routeSyncService, environment);
        start();
    }

    public void start() {
        watcherData(NacosPathConstants.ROUTE_DATA_ID, this::updateRouteList);
        watcherData(NacosPathConstants.API_DATA_ID, this::updateApiList);
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
