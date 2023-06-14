package com.ahcloud.gateway.server.infrastructure.gateway.loader;

import com.ahcloud.gateway.core.infrastructure.repository.bean.GatewayRouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;

/**
 * @program: ahcloud-gateway
 * @description: 路由数据加载
 * @author: YuKai Fan
 * @create: 2023/6/13 16:24
 **/
public interface RouteLoader {

    /**
     * 数据加载
     */
    void loader(GatewayRouteDefinition gatewayRouteDefinition);
}
