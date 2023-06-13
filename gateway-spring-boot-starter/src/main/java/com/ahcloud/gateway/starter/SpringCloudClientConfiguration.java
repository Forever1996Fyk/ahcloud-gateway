package com.ahcloud.gateway.starter;

import com.ahcloud.gateway.client.constant.GatewayConstants;
import com.ahcloud.gateway.register.common.config.PropertiesConfiguration;
import com.ahcloud.gateway.starter.listener.SpringCloudClientEventListener;
import com.ahcloud.gateway.starter.repository.GatewayClientNacosRegisterRepository;
import com.ahcloud.gateway.starter.repository.GatewayClientRegisterRepository;
import com.ahcloud.gateway.starter.shutdown.GatewayClientShutdownHook;
import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Properties;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/2/3 11:17
 **/
@Configuration
public class SpringCloudClientConfiguration {


    @Bean
    public SpringCloudClientEventListener springCloudClientEventListener(
            final PropertiesConfiguration clientConfig,
            final Environment env,
            final GatewayClientRegisterRepository gatewayClientRegisterRepository) {
        return new SpringCloudClientEventListener(clientConfig, env, gatewayClientRegisterRepository);
    }

    @Bean
    public PropertiesConfiguration propertiesConfiguration(NacosDiscoveryProperties properties) {
        PropertiesConfiguration configuration = new PropertiesConfiguration();
        Properties nacosProperties = properties.getNacosProperties();
        nacosProperties.setProperty(GatewayConstants.DISCOVERY_GROUP, properties.getGroup());
        configuration.setProps(nacosProperties);
        return configuration;
    }

    @Bean
    public GatewayClientRegisterRepository gatewayClientRegisterRepository(PropertiesConfiguration propertiesConfiguration) {
        GatewayClientNacosRegisterRepository repository = new GatewayClientNacosRegisterRepository();
        repository.init(propertiesConfiguration);
        GatewayClientShutdownHook.set(repository, propertiesConfiguration.getProps());
        return repository;
    }
}
