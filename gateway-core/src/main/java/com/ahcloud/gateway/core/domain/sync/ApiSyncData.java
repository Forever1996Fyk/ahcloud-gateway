package com.ahcloud.gateway.core.domain.sync;

import com.ahcloud.gateway.core.infrastructure.gateway.enums.DataEventTypeEnum;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/5/29 21:59
 **/
public class ApiSyncData implements BaseSyncData {

    /**
     * 路由id
     */
    private final String apiCode;

    private final DataEventTypeEnum eventType;

    public ApiSyncData(String apiCode, DataEventTypeEnum eventType) {
        this.apiCode = apiCode;
        this.eventType = eventType;
    }

    public ApiSyncData() {
        this.apiCode = "";
        this.eventType = DataEventTypeEnum.REFRESH;
    }

    public String getApiCode() {
        return apiCode;
    }

    @Override
    public DataEventTypeEnum getEventType() {
        return eventType;
    }
}
