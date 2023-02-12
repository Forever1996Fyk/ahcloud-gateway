package com.ahcloud.gateway.server.infrastructure.gateway.listener;

import com.ahcloud.gateway.server.infrastructure.gateway.enums.DataEventTypeEnum;
import org.springframework.cloud.gateway.route.RouteDefinition;

import java.util.List;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/2/6 15:39
 **/
public interface DataChangeListener {

    /**
     * 路由变更
     *
     * @param routeDefinitionList
     * @param eventType
     */
    default void onRouteDefinitionChanged(List<RouteDefinition> routeDefinitionList, DataEventTypeEnum eventType) {

    }
}
