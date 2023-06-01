package com.ahcloud.gateway.server.infrastructure.gateway.sync.service;

import com.ahcloud.gateway.client.common.DeletedEnum;
import com.ahcloud.gateway.core.application.service.GatewayRouteDefinitionService;
import com.ahcloud.gateway.core.domain.sync.RouteSyncData;
import com.ahcloud.gateway.core.infrastructure.gateway.enums.DataEventTypeEnum;
import com.ahcloud.gateway.core.infrastructure.repository.bean.GatewayRouteDefinition;
import com.ahcloud.gateway.server.infrastructure.gateway.sync.subscriber.RouteSubscriber;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/5/29 23:07
 **/
@Component
public class RouteSyncService {

    private final RouteSubscriber routeSubscriber;

    private final ApplicationEventPublisher applicationEventPublisher;

    private final GatewayRouteDefinitionService gatewayRouteDefinitionService;

    @Autowired
    public RouteSyncService(RouteSubscriber routeSubscriber, ApplicationEventPublisher applicationEventPublisher, GatewayRouteDefinitionService gatewayRouteDefinitionService) {
        this.routeSubscriber = routeSubscriber;
        this.applicationEventPublisher = applicationEventPublisher;
        this.gatewayRouteDefinitionService = gatewayRouteDefinitionService;
    }

    public void handleRouteSyncData(List<RouteSyncData> routeSyncDataList) {
        for (RouteSyncData routeSyncData : routeSyncDataList) {
            if (DataEventTypeEnum.DELETE == routeSyncData.getEventType()) {
                routeSubscriber.unSubscribe(routeSyncData.getRouteId());
                continue;
            }
            if (DataEventTypeEnum.REFRESH == routeSyncData.getEventType()) {
                List<GatewayRouteDefinition> gatewayRouteDefinitionList = gatewayRouteDefinitionService.list(
                        new QueryWrapper<GatewayRouteDefinition>().lambda()
                                .eq(GatewayRouteDefinition::getEnv, routeSyncData.getEnv())
                                .eq(GatewayRouteDefinition::getDeleted, DeletedEnum.NO.value)
                );
                routeSubscriber.refresh(gatewayRouteDefinitionList);
                return;
            }
            GatewayRouteDefinition gatewayRouteDefinition = gatewayRouteDefinitionService.getOne(
                    new QueryWrapper<GatewayRouteDefinition>().lambda()
                            .eq(GatewayRouteDefinition::getRouteId, routeSyncData.getRouteId())
                            .eq(GatewayRouteDefinition::getEnv, routeSyncData.getEnv())
                            .eq(GatewayRouteDefinition::getDeleted, DeletedEnum.NO.value)
            );
            if (Objects.isNull(gatewayRouteDefinition)) {
                continue;
            }
            routeSubscriber.onSubscribe(gatewayRouteDefinition);
        }
        this.applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));
    }
}
