package com.ahcloud.gateway.core.infrastructure.gateway.register.instance;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/6/14 11:33
 **/
public interface GatewayClientServerInstanceService {

    /**
     * 校验健康实例
     * @param appName
     * @param env
     * @return
     */
    boolean checkHealthInstance(String appName, String env);
}
