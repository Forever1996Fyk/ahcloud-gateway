package com.ahcloud.gateway.core.infrastructure.gateway.register.service;

import com.ahcloud.gateway.client.common.DeletedEnum;
import com.ahcloud.gateway.client.enums.RpcTypeEnum;
import com.ahcloud.gateway.core.application.helper.GatewayApiMetadataHelper;
import com.ahcloud.gateway.core.application.helper.GatewayRouteHelper;
import com.ahcloud.gateway.core.application.service.GatewayApiMetaDataService;
import com.ahcloud.gateway.core.application.service.GatewayRouteDefinitionService;
import com.ahcloud.gateway.core.domain.route.dto.RouteDefinitionDTO;
import com.ahcloud.gateway.core.infrastructure.gateway.enums.ConfigGroupEnum;
import com.ahcloud.gateway.core.infrastructure.gateway.enums.DataEventTypeEnum;
import com.ahcloud.gateway.core.infrastructure.gateway.listener.event.DataChangedEvent;
import com.ahcloud.gateway.core.infrastructure.repository.bean.GatewayApiMetaData;
import com.ahcloud.gateway.core.infrastructure.repository.bean.GatewayRouteDefinition;
import com.ahcloud.gateway.register.common.dto.MetaDataRegisterDTO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Objects;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/5/29 10:39
 **/
@Service("springCloud")
public class GatewayClientRegisterSpringCloudServiceImpl extends AbstractGatewayClientRegisterService {
    @Resource
    private ApplicationEventPublisher publisher;
    @Resource
    private GatewayApiMetaDataService gatewayApiMetaDataService;
    @Resource
    private GatewayRouteDefinitionService gatewayRouteDefinitionService;

    @Override
    public String rpcType() {
        return RpcTypeEnum.SPRING_CLOUD.getName();
    }

    @Override
    protected void doRegisterRoute(RouteDefinitionDTO routeDefinitionDTO) {
        GatewayRouteDefinition existedGateRouteDefinition = gatewayRouteDefinitionService.getOne(
                new QueryWrapper<GatewayRouteDefinition>().lambda()
                        .eq(GatewayRouteDefinition::getRouteId, routeDefinitionDTO.getId())
                        .eq(GatewayRouteDefinition::getDeleted, DeletedEnum.NO.value)
        );
        GatewayRouteDefinition routeDefinition = GatewayRouteHelper.convert(routeDefinitionDTO);
        DataEventTypeEnum eventType;
        if (Objects.isNull(existedGateRouteDefinition)) {
            try {
                gatewayRouteDefinitionService.save(routeDefinition);
                eventType = DataEventTypeEnum.CREATE;
            } catch (DuplicateKeyException e) {
                // 可能存在并发问题，目前可以忽略不处理
                return;
            }
        } else {
            routeDefinition.setId(existedGateRouteDefinition.getId());
            gatewayRouteDefinitionService.updateById(routeDefinition);
            eventType = DataEventTypeEnum.UPDATE;
        }
        // 发布路由
        publisher.publishEvent(new DataChangedEvent(Collections.singletonList(routeDefinitionDTO), eventType, ConfigGroupEnum.LOCAL_ROUTE));
    }

    @Override
    protected void doRegisterMetadata(MetaDataRegisterDTO metaDataRegisterDTO) {
        GatewayApiMetaData existedGatewayApiMetaData = gatewayApiMetaDataService.getOne(
                new QueryWrapper<GatewayApiMetaData>().lambda()
                        .eq(GatewayApiMetaData::getEnv, metaDataRegisterDTO.getEnv())
                        .eq(GatewayApiMetaData::getAppName, metaDataRegisterDTO.getAppName())
                        .eq(GatewayApiMetaData::getApiPath, metaDataRegisterDTO.getApiPath())
                        .eq(GatewayApiMetaData::getServiceId, metaDataRegisterDTO.getServiceId())
                        .eq(GatewayApiMetaData::getDeleted, DeletedEnum.NO.value)
        );
        GatewayApiMetaData gatewayApiMetaData = GatewayApiMetadataHelper.convert(metaDataRegisterDTO);
        if (Objects.isNull(existedGatewayApiMetaData)) {
            try {
                gatewayApiMetaDataService.save(gatewayApiMetaData);
            } catch (DuplicateKeyException e) {
                // 可能存在并发问题，目前可以忽略不处理
            }
        } else {
            gatewayApiMetaData.setId(existedGatewayApiMetaData.getId());
            gatewayApiMetaDataService.updateById(gatewayApiMetaData);
        }
    }
}
