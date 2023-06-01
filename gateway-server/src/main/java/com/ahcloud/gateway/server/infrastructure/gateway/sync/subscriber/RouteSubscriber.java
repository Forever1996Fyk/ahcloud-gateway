package com.ahcloud.gateway.server.infrastructure.gateway.sync.subscriber;

import com.ahcloud.gateway.core.infrastructure.repository.bean.GatewayRouteDefinition;

import java.util.List;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/5/29 19:21
 **/
public interface RouteSubscriber {

    /**
     * On subscribe.
     * @param routeDefinition
     */
    void onSubscribe(GatewayRouteDefinition routeDefinition);

    /**
     * Un subscribe.
     * @param routeId
     */
    void unSubscribe(String routeId);

    /**
     * Refresh
     * @param gatewayRouteDefinitionList
     */
    default void refresh(List<GatewayRouteDefinition> gatewayRouteDefinitionList) {

    }
}
