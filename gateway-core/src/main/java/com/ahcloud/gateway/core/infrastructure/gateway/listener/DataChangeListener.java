package com.ahcloud.gateway.core.infrastructure.gateway.listener;

import com.ahcloud.gateway.core.domain.api.dto.ApiDefinitionDTO;
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
     * 路由变更
     *
     * @param routeDefinitionList
     * @param eventType
     */
    default void onRouteDefinitionChanged(List<RouteDefinitionDTO> routeDefinitionList, DataEventTypeEnum eventType) {

    }

    /**
     * api接口变更
     * @param apiDefinitionDTOList
     * @param eventType
     */
    default void onApiRefreshChanged(List<ApiDefinitionDTO> apiDefinitionDTOList, DataEventTypeEnum eventType) {

    }
}
