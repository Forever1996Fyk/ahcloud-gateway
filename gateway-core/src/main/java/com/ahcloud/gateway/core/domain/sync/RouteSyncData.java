package com.ahcloud.gateway.core.domain.sync;

import com.ahcloud.gateway.core.infrastructure.gateway.enums.DataEventTypeEnum;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/5/29 21:58
 **/
public class RouteSyncData implements BaseSyncData{

    /**
     * 路由id
     */
    private String routeId;

    private DataEventTypeEnum eventType;

    public RouteSyncData() {
    }

    /**
     * 当前环境
     */
    private String env;

    public RouteSyncData(String routeId, DataEventTypeEnum eventType) {
        this.routeId = routeId;
        this.eventType = eventType;
    }

    public RouteSyncData(String routeId, DataEventTypeEnum eventType, String env) {
        this.routeId = routeId;
        this.eventType = eventType;
        this.env = env;
    }

    public String getRouteId() {
        return routeId;
    }

    public String getEnv() {
        return env;
    }

    @Override
    public DataEventTypeEnum getEventType() {
        return this.eventType;
    }
}
