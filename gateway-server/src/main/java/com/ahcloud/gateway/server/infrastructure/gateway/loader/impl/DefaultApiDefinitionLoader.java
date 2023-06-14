package com.ahcloud.gateway.server.infrastructure.gateway.loader.impl;

import com.ahcloud.gateway.client.common.DeletedEnum;
import com.ahcloud.gateway.client.enums.ApiTypeEnum;
import com.ahcloud.gateway.core.application.service.GatewayApiService;
import com.ahcloud.gateway.core.infrastructure.repository.bean.GatewayApi;
import com.ahcloud.gateway.server.infrastructure.gateway.loader.ApiDefinitionLoader;
import com.ahcloud.gateway.server.infrastructure.gateway.sync.subscriber.ApiSubscriber;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/6/13 16:58
 **/
@Slf4j
@Component
public class DefaultApiDefinitionLoader implements ApiDefinitionLoader, InitializingBean {
    private final ApiSubscriber apiSubscriber;
    private final GatewayApiService gatewayApiService;

    public DefaultApiDefinitionLoader(ApiSubscriber apiSubscriber, GatewayApiService gatewayApiService) {
        this.apiSubscriber = apiSubscriber;
        this.gatewayApiService = gatewayApiService;
    }

    @Override
    public void loader(GatewayApi gatewayApi) {
        apiSubscriber.onSubscribe(gatewayApi);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("项目启动默认加载api信息");
        List<GatewayApi> gatewayApiList = gatewayApiService.list(
                new QueryWrapper<GatewayApi>().lambda()
                        .eq(GatewayApi::getApiType, ApiTypeEnum.WEB_SYSTEM.getType())
                        .eq(GatewayApi::getDeleted, DeletedEnum.NO.value)
        );
        gatewayApiList.forEach(this::loader);
    }
}
