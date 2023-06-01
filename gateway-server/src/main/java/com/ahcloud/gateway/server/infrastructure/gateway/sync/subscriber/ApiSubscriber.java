package com.ahcloud.gateway.server.infrastructure.gateway.sync.subscriber;

import com.ahcloud.gateway.core.infrastructure.repository.bean.GatewayApi;

import java.util.List;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/5/29 19:22
 **/
public interface ApiSubscriber {

    /**
     * On subscribe.
     * @param gatewayApi
     */
    void onSubscribe(GatewayApi gatewayApi);

    /**
     * Un subscribe.
     * @param gatewayApi
     */
    void unSubscribe(GatewayApi gatewayApi);

    /**
     * Refresh
     * @param gatewayApiList
     */
    default void refresh(List<GatewayApi> gatewayApiList) {

    }
}
