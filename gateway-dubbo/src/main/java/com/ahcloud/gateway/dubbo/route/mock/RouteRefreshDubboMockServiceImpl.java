package com.ahcloud.gateway.dubbo.route.mock;

import com.ahcloud.common.result.RpcResult;
import com.ahcloud.gateway.client.dubbo.route.dto.RouteRegisterDTO;
import com.ahcloud.gateway.dubbo.route.RouteRefreshDubboService;
import lombok.extern.slf4j.Slf4j;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/2/21 20:44
 **/
@Slf4j
public class RouteRefreshDubboMockServiceImpl implements RouteRefreshDubboService {

    @Override
    public RpcResult<Boolean> routeRegister(RouteRegisterDTO registerDTO) {
        log.error("GatewayServer Route Register service, method is routeRegister offline");
        return RpcResult.ofSuccess(false);
    }
}
