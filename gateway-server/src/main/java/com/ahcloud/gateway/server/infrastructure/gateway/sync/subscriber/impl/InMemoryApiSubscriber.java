package com.ahcloud.gateway.server.infrastructure.gateway.sync.subscriber.impl;

import com.ahcloud.gateway.common.sentinel.api.SentinelApiDefinitionHandler;
import com.ahcloud.gateway.core.application.helper.GatewayApiHelper;
import com.ahcloud.gateway.core.domain.api.dto.ApiDefinitionDTO;
import com.ahcloud.gateway.core.infrastructure.repository.bean.GatewayApi;
import com.ahcloud.gateway.server.infrastructure.gateway.sync.cache.ApiDataCache;
import com.ahcloud.gateway.server.infrastructure.gateway.sync.subscriber.ApiSubscriber;
import com.alibaba.csp.sentinel.adapter.gateway.common.SentinelGatewayConstants;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiDefinition;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiPathPredicateItem;
import com.google.common.base.Throwables;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/5/29 23:24
 **/
@Slf4j
@Component
public class InMemoryApiSubscriber implements ApiSubscriber {

    private final SentinelApiDefinitionHandler sentinelApiDefinitionHandler;

    @Autowired
    public InMemoryApiSubscriber(SentinelApiDefinitionHandler sentinelApiDefinitionHandler) {
        this.sentinelApiDefinitionHandler = sentinelApiDefinitionHandler;
    }

    @Override
    public void onSubscribe(GatewayApi gatewayApi) {
        ApiDefinitionDTO apiDefinitionDTO = GatewayApiHelper.convertToDTO(gatewayApi);
        ApiDataCache instance = ApiDataCache.getInstance();
        instance.cache(apiDefinitionDTO);

        // Sentinel api分组注册
        try {
            Set<ApiDefinition> apiDefinitionSet = Sets.newHashSet();
            ApiDefinition apiDefinition = new ApiDefinition(gatewayApi.getApiCode())
                    .setPredicateItems(Sets.newHashSet(
                            new ApiPathPredicateItem()
                                    .setPattern(gatewayApi.getApiPath())
                                    .setMatchStrategy(SentinelGatewayConstants.PARAM_MATCH_STRATEGY_PREFIX)
                    ));
            apiDefinitionSet.add(apiDefinition);
            sentinelApiDefinitionHandler.register(apiDefinitionSet);
        } catch (Exception e) {
            log.info("sentinel register failed, reason is {}", Throwables.getStackTraceAsString(e));
        }
    }

    @Override
    public void unSubscribe(GatewayApi gatewayApi) {
        ApiDefinitionDTO apiDefinitionDTO = GatewayApiHelper.convertToDTO(gatewayApi);
        ApiDataCache instance = ApiDataCache.getInstance();
        instance.remove(apiDefinitionDTO);
    }

    @Override
    public void refresh(List<GatewayApi> gatewayApiList) {
        for (GatewayApi gatewayApi : gatewayApiList) {
            this.unSubscribe(gatewayApi);
            this.onSubscribe(gatewayApi);
        }
    }
}
