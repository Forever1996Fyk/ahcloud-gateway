package com.ahcloud.gateway.server.infrastructure.config;

import com.ahcloud.common.utils.CollectionUtils;
import com.ahcloud.gateway.core.infrastructure.gateway.register.disruptor.publisher.RegisterClientServerDisruptorPublisher;
import com.ahcloud.gateway.core.infrastructure.gateway.register.repository.GatewayClientServerRegisterRepository;
import com.ahcloud.gateway.core.infrastructure.gateway.register.repository.NacosClientServerRegisterRepository;
import com.ahcloud.gateway.core.infrastructure.gateway.register.service.GatewayClientRegisterService;
import com.ahcloud.gateway.register.common.config.PropertiesConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.List;
import java.util.Map;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/5/29 15:51
 **/
@Configuration
public class RegisterCenterConfiguration {

    /**
     * gateway client server register repository server register repository
     * @param gatewayClientRegisterServices
     * @param propertiesConfiguration
     * @return
     */
    @Bean(destroyMethod = "close")
    public GatewayClientServerRegisterRepository gatewayClientServerRegisterRepository(
            final List<GatewayClientRegisterService> gatewayClientRegisterServices, final PropertiesConfiguration propertiesConfiguration, final Environment environment) {
        NacosClientServerRegisterRepository repository = new NacosClientServerRegisterRepository(environment);
        RegisterClientServerDisruptorPublisher publisher = RegisterClientServerDisruptorPublisher.getInstance();
        Map<String, GatewayClientRegisterService> registerServiceMap = CollectionUtils.convertMap(gatewayClientRegisterServices, GatewayClientRegisterService::rpcType);
        publisher.start(registerServiceMap);
        repository.init(publisher, propertiesConfiguration);
        return repository;
    }
}
