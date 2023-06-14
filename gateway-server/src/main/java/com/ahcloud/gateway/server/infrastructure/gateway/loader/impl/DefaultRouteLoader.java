package com.ahcloud.gateway.server.infrastructure.gateway.loader.impl;

import com.ahcloud.gateway.client.common.DeletedEnum;
import com.ahcloud.gateway.client.enums.RouteTypeEnum;
import com.ahcloud.gateway.core.application.service.GatewayRouteDefinitionService;
import com.ahcloud.gateway.core.infrastructure.repository.bean.GatewayRouteDefinition;
import com.ahcloud.gateway.server.infrastructure.gateway.loader.RouteLoader;
import com.ahcloud.gateway.server.infrastructure.gateway.sync.subscriber.RouteSubscriber;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/6/13 16:25
 **/
@Slf4j
@Component
public class DefaultRouteLoader implements RouteLoader, InitializingBean {
    private final RouteSubscriber routeSubscriber;
    private final GatewayRouteDefinitionService gatewayRouteDefinitionService;

    public DefaultRouteLoader(RouteSubscriber routeSubscriber, GatewayRouteDefinitionService gatewayRouteDefinitionService) {
        this.routeSubscriber = routeSubscriber;
        this.gatewayRouteDefinitionService = gatewayRouteDefinitionService;
    }

    @Override
    public void loader(GatewayRouteDefinition gatewayRouteDefinition) {
        routeSubscriber.onSubscribe(gatewayRouteDefinition);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("项目启动默认加载路由信息");
        List<GatewayRouteDefinition> gatewayRouteDefinitions = gatewayRouteDefinitionService.list(
                new QueryWrapper<GatewayRouteDefinition>().lambda()
                        .eq(GatewayRouteDefinition::getRouteType, RouteTypeEnum.SYSTEM.getType())
                        .eq(GatewayRouteDefinition::getDeleted, DeletedEnum.NO.value)
        );
        gatewayRouteDefinitions.forEach(this::loader);
    }
}
