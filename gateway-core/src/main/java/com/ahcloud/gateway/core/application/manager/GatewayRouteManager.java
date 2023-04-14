package com.ahcloud.gateway.core.application.manager;

import com.ahcloud.gateway.client.common.DeletedEnum;
import com.ahcloud.gateway.client.enums.ApiStatusEnum;
import com.ahcloud.gateway.core.application.helper.RouteHelper;
import com.ahcloud.gateway.core.application.service.GatewayApiService;
import com.ahcloud.gateway.core.infrastructure.gateway.enums.ConfigGroupEnum;
import com.ahcloud.gateway.core.infrastructure.gateway.enums.DataEventTypeEnum;
import com.ahcloud.gateway.core.infrastructure.gateway.listener.event.DataChangedEvent;
import com.ahcloud.gateway.core.infrastructure.repository.bean.GatewayApi;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

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
                        .eq(GatewayApi::getStatus, ApiStatusEnum.ONLINE.getStatus())
                        .eq(GatewayApi::getDeleted, DeletedEnum.NO.value)
        );
        applicationEventPublisher.publishEvent(
                new DataChangedEvent(RouteHelper.buildRoutes(gatewayApiList), DataEventTypeEnum.REFRESH, ConfigGroupEnum.ROUTE)
        );
    }
}
