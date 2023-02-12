package com.ahcloud.gateway.server.api.controller;

import com.ahcloud.gateway.server.application.manager.GatewayRouteManager;
import com.ahcloud.gateway.server.domain.response.GatewayResponseResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/2/8 10:03
 **/
@RestController
@RequestMapping("/route")
public class RouteController {
    @Resource
    private GatewayRouteManager gatewayRouteManager;

    /**
     * 刷新路由
     * @return
     */
    @PostMapping("/refreshRoute")
    public GatewayResponseResult refreshRoute() {
        gatewayRouteManager.refreshRoute();
        return GatewayResponseResult.ofSuccess();
    }
}
