package com.ahcloud.gateway.server.infrastructure.gateway.loader;

import com.ahcloud.gateway.core.infrastructure.repository.bean.GatewayApi;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/6/13 16:57
 **/
public interface ApiDefinitionLoader {

    /**
     * 加载api
     * @param gatewayApi
     */
    void loader(GatewayApi gatewayApi);
}
