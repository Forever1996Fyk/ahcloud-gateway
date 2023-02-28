package com.ahcloud.gateway.server.dubbo;

import com.ahcloud.common.result.RpcResult;
import com.ahcloud.gateway.client.dubbo.route.dto.RouteRegisterDTO;
import com.ahcloud.gateway.core.application.helper.RouteHelper;
import com.ahcloud.gateway.core.infrastructure.gateway.enums.ConfigGroupEnum;
import com.ahcloud.gateway.core.infrastructure.gateway.enums.DataEventTypeEnum;
import com.ahcloud.gateway.core.infrastructure.gateway.listener.event.DataChangedEvent;
import com.ahcloud.gateway.dubbo.route.RouteRefreshDubboService;
import com.ahcloud.gateway.server.infrastructure.gateway.service.GatewayService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.context.ApplicationEventPublisher;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/2/12 21:48
 **/
@DubboService(version = "1.0.0", timeout = 6000)
public class RouteRefreshDubboServiceImpl implements RouteRefreshDubboService {
    @Resource
    private GatewayService gatewayService;
    @Resource
    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public RpcResult<Boolean> routeRegister(RouteRegisterDTO registerDTO) {
        gatewayService.existedRoute(registerDTO.getServiceId())
                .map(item -> {
                    if (!item) {
                        applicationEventPublisher.publishEvent(
                                new DataChangedEvent(RouteHelper.buildRoute(registerDTO.getServiceId()), DataEventTypeEnum.CREATE, ConfigGroupEnum.ROUTE)
                        );
                    }
                    return Mono.empty();
                }).subscribe();
        return RpcResult.ofSuccess(true);
    }
}
