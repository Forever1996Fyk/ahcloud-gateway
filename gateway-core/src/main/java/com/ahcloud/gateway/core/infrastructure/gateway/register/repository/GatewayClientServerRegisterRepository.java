package com.ahcloud.gateway.core.infrastructure.gateway.register.repository;


import com.ahcloud.gateway.core.infrastructure.gateway.register.disruptor.publisher.GatewayClientServerRegisterPublisher;
import com.ahcloud.gateway.register.common.config.PropertiesConfiguration;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/5/26 16:43
 **/
public interface GatewayClientServerRegisterRepository {

    /**
     * 初始化操作
     * @param configuration
     */
    default void init(PropertiesConfiguration configuration) {

    }

    /**
     * 初始化操作
     * @param publisher
     * @param configuration
     */
    default void init(GatewayClientServerRegisterPublisher publisher, PropertiesConfiguration configuration) {

    }

    /**
     * 初始化操作
     * @param publisher
     * @param configuration
     */
    default void initJustRoute(GatewayClientServerRegisterPublisher publisher, PropertiesConfiguration configuration) {

    }

    /**
     * 初始化操作
     * @param publisher
     * @param configuration
     */
    default void initJustMeta(GatewayClientServerRegisterPublisher publisher, PropertiesConfiguration configuration) {

    }


    /**
     * 关闭
     */
    default void close() {

    }
}
