package com.ahcloud.gateway.core.infrastructure.gateway.register.disruptor.publisher;

import com.ahcloud.gateway.register.common.type.DataTypeParent;

import java.util.Collection;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/5/26 17:21
 **/
public interface GatewayClientServerRegisterPublisher {

    /**
     * 发布数据
     * @param dataList
     */
    void publish(Collection<? extends DataTypeParent> dataList);

    /**
     * 发布数据
     * @param data
     */
    void publish(DataTypeParent data);

    /**
     * 关闭
     */
    default void close() {

    }
}
