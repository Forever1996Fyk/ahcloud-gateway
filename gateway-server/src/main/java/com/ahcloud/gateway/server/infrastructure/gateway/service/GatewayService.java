package com.ahcloud.gateway.server.infrastructure.gateway.service;

import org.springframework.cloud.gateway.route.RouteDefinition;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/2/6 09:38
 **/
public interface GatewayService {

    /**
     * 路由是否存在
     * @param serviceId
     * @return
     */
    Mono<Boolean> existedRoute(String serviceId);

    /**
     * 新增路由
     * @param routeDefinition
     * @return
     */
    Mono<Boolean> addRoute(RouteDefinition routeDefinition);

    /**
     * 新增路由
     * @param routeDefinitionList
     * @return
     */
    Mono<Boolean> batchAddRoute(List<RouteDefinition> routeDefinitionList);

    /**
     * 批量删除路由
     * @param serviceIdList
     * @return
     */
    boolean batchDeleteRoute(List<String> serviceIdList);

    /**
     * 删除路由
     * @param serviceId
     * @return boolean
     */
    Mono<Boolean> deleteRoute(String serviceId);

    /**
     * 更新路由
     * @param routeDefinition
     * @return
     */
    Mono<Boolean> updateRoute(RouteDefinition routeDefinition);

    /**
     * 全量刷新路由
     * @param routeDefinitionList
     * @return boolean
     */
    Mono<Void> refreshRoute(List<RouteDefinition> routeDefinitionList);
}
