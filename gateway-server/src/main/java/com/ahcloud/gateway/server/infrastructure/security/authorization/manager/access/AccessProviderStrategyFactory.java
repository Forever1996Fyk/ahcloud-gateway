package com.ahcloud.gateway.server.infrastructure.security.authorization.manager.access;

import com.ahcloud.gateway.client.enums.AppPlatformEnum;
import com.ahcloud.gateway.server.infrastructure.exception.GatewayException;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Objects;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/1/18 20:06
 **/
@Slf4j
public class AccessProviderStrategyFactory {
    private static final Map<AppPlatformEnum, AccessProvider> FACTORY = Maps.newHashMap();

    private static final String EXCEPTION_MSG_NOT_SUPPORT = "AccessProvider处理策略不支持";
    private static final String EXCEPTION_MSG_NULL = "AccessProvider处理策略为空";
    private static final String EXCEPTION_MSG_REPEATED = "AccessProvider处理策略已存在";

    /**
     * 注册策略
     *
     * @see AppPlatformEnum
     * @param strategy
     */
    public static void register(AppPlatformEnum platformEnum, AccessProvider strategy) {
        if (Objects.isNull(platformEnum)) {
            throw new GatewayException(EXCEPTION_MSG_NOT_SUPPORT);
        }
        if (Objects.isNull(strategy)) {
            throw new GatewayException(EXCEPTION_MSG_NULL);
        }

        if (FACTORY.containsKey(platformEnum)) {
            throw new GatewayException(EXCEPTION_MSG_REPEATED);
        }
        log.info("AccessProvider策略注册, 类型为{},", platformEnum);
        FACTORY.put(platformEnum, strategy);
    }

    /**
     * 查找对应策略
     *
     * @see AppPlatformEnum
     *
     * @return
     */
    public static AccessProvider getStrategy(AppPlatformEnum platformEnum) {
        if (Objects.isNull(platformEnum)) {
            throw new GatewayException(EXCEPTION_MSG_NOT_SUPPORT);
        }

        AccessProvider strategy = FACTORY.get(platformEnum);
        if (Objects.isNull(strategy)) {
            throw new GatewayException(EXCEPTION_MSG_NULL);
        }
        return strategy;
    }
}
