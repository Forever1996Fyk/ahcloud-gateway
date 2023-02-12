package com.ahcloud.gateway.server.application.manager;

import com.ahcloud.gateway.client.common.DeletedEnum;
import com.ahcloud.gateway.client.enums.ApiStatusEnum;
import com.ahcloud.gateway.server.application.helper.RouteHelper;
import com.ahcloud.gateway.server.application.service.GatewayApiService;
import com.ahcloud.gateway.server.domain.route.form.RouteUpdateForm;
import com.ahcloud.gateway.server.domain.route.vo.RouteVo;
import com.ahcloud.gateway.server.infrastructure.gateway.enums.ConfigGroupEnum;
import com.ahcloud.gateway.server.infrastructure.gateway.enums.DataEventTypeEnum;
import com.ahcloud.gateway.server.infrastructure.gateway.listener.event.DataChangedEvent;
import com.ahcloud.gateway.server.infrastructure.gateway.service.GatewayService;
import com.ahcloud.gateway.server.infrastructure.repository.bean.GatewayApi;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.List;

/**
 * @program: ahcloud-gateway
 * @description: 网关路由管理
 * @author: YuKai Fan
 * @create: 2023/2/8 10:02
 **/
@Slf4j
@Component
public class GatewayRouteManager {
    @Resource
    private GatewayService gatewayService;
    @Resource
    private GatewayApiService gatewayApiService;
    @Resource
    private ApplicationEventPublisher applicationEventPublisher;

    /**
     * 刷新接口
     */
    public void refreshRoute() {
        List<GatewayApi> gatewayApiList = gatewayApiService.list(
                new QueryWrapper<GatewayApi>().lambda()
                        .select(GatewayApi::getServiceId)
                        .groupBy(GatewayApi::getServiceId)
                        .eq(GatewayApi::getStatus, ApiStatusEnum.NORMAL.getStatus())
                        .eq(GatewayApi::getDeleted, DeletedEnum.NO.value)
        );
        applicationEventPublisher.publishEvent(
                new DataChangedEvent(RouteHelper.buildRoutes(gatewayApiList), DataEventTypeEnum.REFRESH, ConfigGroupEnum.ROUTE)
        );
    }

    /**
     * 更新路由
     */
    public void updateRoute(RouteUpdateForm form) {
        gatewayService.existedRoute(form.getServiceId())
                .map(item -> {
                    if (item) {
                        applicationEventPublisher.publishEvent(
                                new DataChangedEvent(RouteHelper.buildRoute(form.getServiceId()), DataEventTypeEnum.UPDATE, ConfigGroupEnum.ROUTE)
                        );
                    }
                    return Mono.empty();
                }).subscribe();
    }

    /**
     * 删除路由
     */
    public void deleteRoute(String serviceId) {
        gatewayService.existedRoute(serviceId)
                .map(item -> {
                    if (item) {
                        applicationEventPublisher.publishEvent(
                                new DataChangedEvent(RouteHelper.buildRoute(serviceId), DataEventTypeEnum.DELETE, ConfigGroupEnum.ROUTE)
                        );
                    }
                    return Mono.empty();
                }).subscribe();
    }
}
