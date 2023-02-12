package com.ahcloud.gateway.server.infrastructure.gateway.listener;

import com.ahcloud.common.utils.JsonUtils;
import com.ahcloud.gateway.server.infrastructure.gateway.enums.DataEventTypeEnum;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.springframework.cloud.gateway.route.RouteDefinition;

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

    private static final Map<String, RouteDefinition> ROUTE_DEFINITION_MAP = Maps.newConcurrentMap();

    protected AbstractListDataChangedListener(ChangeData changeData) {
        this.changeData = changeData;
    }

    @Override
    public void onRouteDefinitionChanged(List<RouteDefinition> routeDefinitionList, DataEventTypeEnum eventType) {
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
            ROUTE_DEFINITION_MAP.put(entry.getKey(), JsonUtils.mapToBean(entry.getValue(), RouteDefinition.class));
        }
        ROUTE_DEFINITION_MAP.keySet().removeAll(set);
    }

    public static class ChangeData {

        /**
         * route definition data id
         */
        private final String routeDataId;

        public ChangeData(String routeDataId) {
            this.routeDataId = routeDataId;
        }

        public String getRouteDataId() {
            return routeDataId;
        }
    }
}
