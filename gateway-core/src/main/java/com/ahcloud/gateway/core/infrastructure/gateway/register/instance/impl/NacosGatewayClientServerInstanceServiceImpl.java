package com.ahcloud.gateway.core.infrastructure.gateway.register.instance.impl;

import com.ahcloud.gateway.core.infrastructure.gateway.register.instance.GatewayClientServerInstanceService;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/6/14 11:34
 **/
@Component("gatewayClientServerInstanceService")
public class NacosGatewayClientServerInstanceServiceImpl implements GatewayClientServerInstanceService {
    @Resource
    private NamingService namingService;

    @Override
    public boolean checkHealthInstance(String appName, String env) {
        try {
            Instance instance = namingService.selectOneHealthyInstance(appName, env);
            return Objects.isNull(instance);
        } catch (NacosException e) {
            return true;
        }
    }
}
