package com.ahcloud.gateway.core.infrastructure.gateway.enums;

/**
 * @program: ahcloud-gateway
 * @description: 数组变更类型
 * @author: YuKai Fan
 * @create: 2023/2/6 15:34
 **/
public enum DataEventTypeEnum {

    /**
     * delete event.
     */
    DELETE,

    /**
     * insert event.
     */
    CREATE,

    /**
     * update event.
     */
    UPDATE,

    /**
     * REFRESH data event type enum.
     */
    REFRESH,

    ;
}
