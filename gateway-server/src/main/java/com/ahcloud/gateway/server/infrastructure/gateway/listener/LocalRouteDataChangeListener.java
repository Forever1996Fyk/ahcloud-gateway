package com.ahcloud.gateway.server.infrastructure.gateway.listener;

import com.ahcloud.gateway.core.domain.route.dto.RouteDefinitionDTO;
import com.ahcloud.gateway.core.domain.sync.RouteSyncData;
import com.ahcloud.gateway.core.infrastructure.gateway.enums.DataEventTypeEnum;
import com.ahcloud.gateway.core.infrastructure.gateway.listener.DataChangeListener;
import com.ahcloud.gateway.server.infrastructure.gateway.sync.service.RouteSyncService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/5/30 09:04
 **/
public class LocalRouteDataChangeListener implements DataChangeListener {

    private final RouteSyncService routeSyncService;

    public LocalRouteDataChangeListener(RouteSyncService routeSyncService) {
        this.routeSyncService = routeSyncService;
    }

    @Override
    public void onLocalRouteDefinitionChanged(List<RouteDefinitionDTO> routeDefinitionList, DataEventTypeEnum eventType) {
        List<RouteSyncData> routeSyncDataList = routeDefinitionList.stream()
                .map(routeDefinitionDTO -> new RouteSyncData(routeDefinitionDTO.getId(), eventType, routeDefinitionDTO.getEnv())).collect(Collectors.toList());
        routeSyncService.handleRouteSyncData(routeSyncDataList);
    }
}
