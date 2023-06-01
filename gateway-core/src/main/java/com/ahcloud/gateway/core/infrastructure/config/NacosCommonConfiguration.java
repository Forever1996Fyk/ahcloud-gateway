package com.ahcloud.gateway.core.infrastructure.config;

import com.ahcloud.gateway.client.constant.GatewayClientConstants;
import com.ahcloud.gateway.register.common.config.PropertiesConfiguration;
import com.alibaba.cloud.nacos.NacosConfigProperties;
import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.NacosServiceManager;
import com.alibaba.nacos.api.naming.NamingService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/5/30 15:51
 **/
@Configuration
public class NacosCommonConfiguration {

    /**
     * 注入 nacos服务发现， namingService
     * @param nacosServiceManager
     * @param properties
     * @return
     */
    @Bean
    public NamingService namingService(NacosServiceManager nacosServiceManager, NacosDiscoveryProperties properties) {
        return nacosServiceManager.getNamingService(properties.getNacosProperties());
    }

    @Bean
    public PropertiesConfiguration propertiesConfiguration(NacosDiscoveryProperties properties) {
        PropertiesConfiguration configuration = new PropertiesConfiguration();
        Properties nacosProperties = properties.getNacosProperties();
        nacosProperties.setProperty(GatewayClientConstants.DISCOVERY_GROUP, properties.getGroup());
        configuration.setProps(nacosProperties);
        return configuration;
    }
}
