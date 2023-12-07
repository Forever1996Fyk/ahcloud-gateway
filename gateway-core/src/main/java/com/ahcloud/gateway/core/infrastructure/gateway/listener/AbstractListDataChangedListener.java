package com.ahcloud.gateway.core.infrastructure.gateway.listener;

import com.ahcloud.gateway.core.domain.route.dto.RouteDefinitionDTO;
import com.ahcloud.gateway.core.domain.sync.ApiSyncData;
import com.ahcloud.gateway.core.domain.sync.RouteSyncData;
import com.ahcloud.gateway.core.infrastructure.gateway.enums.DataEventTypeEnum;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/2/6 15:53
 **/
public abstract class AbstractListDataChangedListener implements DataChangeListener {

    private final ChangeData changeData;

    protected AbstractListDataChangedListener(ChangeData changeData) {
        this.changeData = changeData;
    }

    @Override
    public void onRemoteRouteDefinitionChanged(List<RouteDefinitionDTO> routeDefinitionList, DataEventTypeEnum eventType) {
        List<RouteSyncData> routeSyncDataList = routeDefinitionList.stream()
                .map(routeDefinitionDTO -> new RouteSyncData(routeDefinitionDTO.getId(), eventType, routeDefinitionDTO.getEnv())).collect(Collectors.toList());
        publishConfig(changeData.getRouteDataId(), routeSyncDataList);
    }

    @Override
    public void onApiRefreshChanged(List<String> apiCodeList, DataEventTypeEnum eventType) {
        List<ApiSyncData> apiSyncDataList = apiCodeList.stream()
                .map(apiCode -> new ApiSyncData(apiCode, 0, eventType))
                .collect(Collectors.toList());
        publishConfig(changeData.getApiDataId(), apiSyncDataList);
    }

    /**
     * 发布配置
     * @param dataId
     * @param data
     */
    protected abstract void publishConfig(String dataId, Object data);


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
