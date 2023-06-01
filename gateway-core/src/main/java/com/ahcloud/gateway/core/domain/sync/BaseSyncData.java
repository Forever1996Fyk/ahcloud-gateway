package com.ahcloud.gateway.core.domain.sync;

import com.ahcloud.gateway.core.infrastructure.gateway.enums.DataEventTypeEnum;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/5/29 21:57
 **/
public interface BaseSyncData {

    /**
     * 事件类型
     * @return
     */
    DataEventTypeEnum getEventType();
}
