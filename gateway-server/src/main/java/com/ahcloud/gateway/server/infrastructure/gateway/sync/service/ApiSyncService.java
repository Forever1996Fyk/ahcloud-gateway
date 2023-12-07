package com.ahcloud.gateway.server.infrastructure.gateway.sync.service;

import com.ahcloud.gateway.client.common.DeletedEnum;
import com.ahcloud.gateway.core.application.service.GatewayApiService;
import com.ahcloud.gateway.core.domain.sync.ApiSyncData;
import com.ahcloud.gateway.core.infrastructure.gateway.enums.DataEventTypeEnum;
import com.ahcloud.gateway.core.infrastructure.repository.bean.GatewayApi;
import com.ahcloud.gateway.server.infrastructure.gateway.sync.subscriber.ApiSubscriber;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/5/30 00:48
 **/
@Component
public class ApiSyncService {

    private final ApiSubscriber apiSubscriber;

    private final GatewayApiService gatewayApiService;

    @Autowired
    public ApiSyncService(ApiSubscriber apiSubscriber, GatewayApiService gatewayApiService) {
        this.apiSubscriber = apiSubscriber;
        this.gatewayApiService = gatewayApiService;
    }

    public void handleApiSyncData(List<ApiSyncData> apiSyncDataList) {
        for (ApiSyncData apiSyncData : apiSyncDataList) {
            DataEventTypeEnum eventType = apiSyncData.getEventType();
            if (DataEventTypeEnum.REFRESH == eventType) {
                List<GatewayApi> gatewayApiList = gatewayApiService.list(
                        new QueryWrapper<GatewayApi>().lambda()
                                .eq(GatewayApi::getDeleted, DeletedEnum.NO.value)
                );
                apiSubscriber.refresh(gatewayApiList);
                return;
            }
            GatewayApi gatewayApi = gatewayApiService.getOne(
                    new QueryWrapper<GatewayApi>().lambda()
                            .eq(GatewayApi::getApiCode, apiSyncData.getApiCode())
                            .eq(GatewayApi::getDeleted, DeletedEnum.NO.value)
            );
            if (Objects.isNull(gatewayApi)) {
                return;
            }
            Long deleted = gatewayApi.getDeleted();
            DeletedEnum deletedEnum = DeletedEnum.valueOf(deleted);
            if (isUpdateApi(eventType, deletedEnum)) {
                apiSubscriber.onSubscribe(gatewayApi);
                continue;
            }
            if ((DataEventTypeEnum.DELETE == eventType) && deletedEnum.isDeleted()) {
                apiSubscriber.unSubscribe(gatewayApi);
            }
        }
    }

    private boolean isUpdateApi(DataEventTypeEnum eventType, DeletedEnum deletedEnum) {
        return (DataEventTypeEnum.CREATE == eventType) || (DataEventTypeEnum.UPDATE == eventType) && !deletedEnum.isDeleted();
    }
}
