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

    /**
     * 版本
     */
    private final Integer version;


    private final DataEventTypeEnum eventType;

    public ApiSyncData(String apiCode, Integer version, DataEventTypeEnum eventType) {
        this.apiCode = apiCode;
        this.version = version;
        this.eventType = eventType;
    }

    public ApiSyncData() {
        this.apiCode = "";
        this.version = 0;
        this.eventType = DataEventTypeEnum.REFRESH;
    }

    public String getApiCode() {
        return apiCode;
    }

    public Integer getVersion() {
        return version;
    }

    @Override
    public DataEventTypeEnum getEventType() {
        return eventType;
    }
}
