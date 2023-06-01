package com.ahcloud.gateway.core.infrastructure.gateway.listener;

import com.ahcloud.gateway.core.domain.route.dto.RouteDefinitionDTO;
import com.ahcloud.gateway.core.infrastructure.gateway.enums.DataEventTypeEnum;

import java.util.List;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/2/6 15:39
 **/
public interface DataChangeListener {

    /**
     * 远程路由变更
     *
     * @param routeDefinitionList
     * @param eventType
     */
    default void onRemoteRouteDefinitionChanged(List<RouteDefinitionDTO> routeDefinitionList, DataEventTypeEnum eventType) {

    }

    /**
     * 本地路由变更
     *
     * @param routeDefinitionList
     * @param eventType
     */
    default void onLocalRouteDefinitionChanged(List<RouteDefinitionDTO> routeDefinitionList, DataEventTypeEnum eventType) {

    }

    /**
     * api接口变更
     * @param apiCodeList
     * @param eventType
     */
    default void onApiRefreshChanged(List<String> apiCodeList, DataEventTypeEnum eventType) {

    }
}
