package com.ahcloud.gateway.common.sentinel.api;

import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiDefinition;

import java.util.Set;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/6/14 16:31
 **/
public interface SentinelApiDefinitionHandler {

    /**
     * api分组注册
     * @param apiDefinitionSet
     */
    void register(Set<ApiDefinition> apiDefinitionSet);
}
