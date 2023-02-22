package com.ahcloud.gateway.dubbo.route;

import com.ahcloud.common.result.RpcResult;
import com.ahcloud.gateway.client.dubbo.route.dto.RouteRegisterDTO;

/**
 * @program: ahcloud-gateway
 * @description: 路由刷新
 * @author: YuKai Fan
 * @create: 2023/2/12 21:45
 **/
public interface RouteRefreshDubboService {

    /**
     * 路由注册
     * @param registerDTO
     * @return
     */
    RpcResult<Boolean> routeRegister(RouteRegisterDTO registerDTO);
}
