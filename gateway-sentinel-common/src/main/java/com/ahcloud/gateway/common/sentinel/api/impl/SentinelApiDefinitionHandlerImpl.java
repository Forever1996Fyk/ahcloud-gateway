package com.ahcloud.gateway.common.sentinel.api.impl;

import com.ahcloud.common.utils.CollectionUtils;
import com.ahcloud.gateway.common.sentinel.api.SentinelApiDefinitionHandler;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiDefinition;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.GatewayApiDefinitionManager;

import java.util.Set;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/6/14 16:32
 **/
public class SentinelApiDefinitionHandlerImpl implements SentinelApiDefinitionHandler {

    @Override
    public void register(Set<ApiDefinition> apiDefinitionSet) {
        if (CollectionUtils.isEmpty(apiDefinitionSet)) {
            return;
        }
        GatewayApiDefinitionManager.loadApiDefinitions(apiDefinitionSet);
    }
}
