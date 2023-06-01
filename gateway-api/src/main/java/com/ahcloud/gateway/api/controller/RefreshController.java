package com.ahcloud.gateway.api.controller;

import com.ahcloud.common.result.ResponseResult;
import com.ahcloud.gateway.core.application.manager.GatewayApiManager;
import com.ahcloud.gateway.core.application.manager.GatewayRouteManager;
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
@RequestMapping("/refresh")
public class RefreshController {
    @Resource
    private GatewayApiManager gatewayApiManager;
    @Resource
    private GatewayRouteManager gatewayRouteManager;

    /**
     * 刷新路由
     * @return
     */
    @PostMapping("/refreshRoute")
    public ResponseResult<Void> refreshRoute() {
        gatewayRouteManager.refreshRoute();
        return ResponseResult.ofSuccess();
    }

    /**
     * 刷新接口
     * @return
     */
    @PostMapping("/refreshApi")
    public ResponseResult<Void> refreshApi() {
        gatewayApiManager.refreshApi();
        return ResponseResult.ofSuccess();
    }
}
