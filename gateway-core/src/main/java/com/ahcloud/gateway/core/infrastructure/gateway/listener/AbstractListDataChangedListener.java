package com.ahcloud.gateway.core.infrastructure.gateway.listener;

import com.ahcloud.common.utils.JsonUtils;
import com.ahcloud.gateway.core.domain.api.dto.ApiRefreshDTO;
import com.ahcloud.gateway.core.domain.route.dto.RouteDefinitionDTO;
import com.ahcloud.gateway.core.infrastructure.gateway.enums.DataEventTypeEnum;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/2/6 15:53
 **/
public abstract class AbstractListDataChangedListener implements DataChangeListener {

    private final ChangeData changeData;

    private static final Map<String, RouteDefinitionDTO> ROUTE_DEFINITION_MAP = Maps.newConcurrentMap();
    private static final Map<String, ApiRefreshDTO> API_REFRESH_MAP = Maps.newConcurrentMap();

    protected AbstractListDataChangedListener(ChangeData changeData) {
        this.changeData = changeData;
    }

    @Override
    public void onRouteDefinitionChanged(List<RouteDefinitionDTO> routeDefinitionList, DataEventTypeEnum eventType) {
        updateRouteDefinitionMap(getConfig(changeData.getRouteDataId()));
        switch (eventType) {
            case REFRESH:
                Set<String> set = Sets.newHashSet(ROUTE_DEFINITION_MAP.keySet());
                routeDefinitionList.forEach(routeDefinition -> {
                    set.remove(routeDefinition.getId());
                    ROUTE_DEFINITION_MAP.put(routeDefinition.getId(), routeDefinition);
                });
                ROUTE_DEFINITION_MAP.keySet().removeAll(set);
                break;
            case DELETE:
                routeDefinitionList.forEach(routeDefinition -> ROUTE_DEFINITION_MAP.remove(routeDefinition.getId()));
                break;
            default:
                routeDefinitionList.forEach(routeDefinition -> ROUTE_DEFINITION_MAP.put(routeDefinition.getId(), routeDefinition));
        }
        publishConfig(changeData.getRouteDataId(), ROUTE_DEFINITION_MAP);
    }

    @Override
    public void onApiRefreshChanged(List<ApiRefreshDTO> apiRefreshDTOList, DataEventTypeEnum eventType) {
        updateApiRefreshMap(getConfig(changeData.getApiDataId()));
        switch (eventType) {
            case REFRESH:
                Set<String> set = Sets.newHashSet(API_REFRESH_MAP.keySet());
                apiRefreshDTOList.forEach(apiRefreshDTO -> {
                    set.remove(apiRefreshDTO.getApiCode());
                    API_REFRESH_MAP.put(apiRefreshDTO.getApiCode(), apiRefreshDTO);
                });
                API_REFRESH_MAP.keySet().removeAll(set);
                break;
            case DELETE:
                apiRefreshDTOList.forEach(apiRefreshDTO -> API_REFRESH_MAP.remove(apiRefreshDTO.getApiCode()));
                break;
            default:
                apiRefreshDTOList.forEach(apiRefreshDTO -> API_REFRESH_MAP.put(apiRefreshDTO.getApiCode(), apiRefreshDTO));
        }
        publishConfig(changeData.getApiDataId(), API_REFRESH_MAP);
    }

    /**
     * 获取配置
     * @param dataId
     * @return
     */
    protected abstract String getConfig(String dataId);

    /**
     * 发布配置
     * @param dataId
     * @param data
     */
    protected abstract void publishConfig(String dataId, Object data);

    private void updateRouteDefinitionMap(final String configInfo) {
        Map<String, Map<String, Object>> map = JsonUtils.stringToMap2(configInfo);
        Set<String> set = Sets.newHashSet(ROUTE_DEFINITION_MAP.keySet());
        for (Map.Entry<String, Map<String, Object>> entry : map.entrySet()) {
            set.remove(entry.getKey());
            ROUTE_DEFINITION_MAP.put(entry.getKey(), JsonUtils.mapToBean(entry.getValue(), RouteDefinitionDTO.class));
        }
        ROUTE_DEFINITION_MAP.keySet().removeAll(set);
    }

    private void updateApiRefreshMap(final String configInfo) {
        Map<String, Map<String, Object>> map = JsonUtils.stringToMap2(configInfo);
        Set<String> set = Sets.newHashSet(API_REFRESH_MAP.keySet());
        for (Map.Entry<String, Map<String, Object>> entry : map.entrySet()) {
            set.remove(entry.getKey());
            API_REFRESH_MAP.put(entry.getKey(), JsonUtils.mapToBean(entry.getValue(), ApiRefreshDTO.class));
        }
        API_REFRESH_MAP.keySet().removeAll(set);
    }


    public static class ChangeData {

        /**
         * route definition data id
         */
        private final String routeDataId;

        private final String apiDataId;

        public ChangeData(String routeDataId, String apiDataId) {
            this.routeDataId = routeDataId;
            this.apiDataId = apiDataId;
        }

        public String getRouteDataId() {
            return routeDataId;
        }

        public String getApiDataId() {
            return apiDataId;
        }
    }
}
