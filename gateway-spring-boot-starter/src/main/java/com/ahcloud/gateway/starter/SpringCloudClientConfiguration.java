package com.ahcloud.gateway.starter;

import com.ahcloud.gateway.starter.configuration.PropertiesConfiguration;
import com.ahcloud.gateway.starter.listener.ApiRegisterEventListener;
import com.ahcloud.gateway.starter.listener.RouteRegisterEventListener;
import com.ahcloud.gateway.starter.listener.SpringCloudClientEventListener;
import com.ahcloud.gateway.starter.repository.GatewayClientDubboRegisterRepository;
import com.ahcloud.gateway.starter.repository.GatewayClientRegisterRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

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
            final Environment env) {
        return new SpringCloudClientEventListener(clientConfig, env);
    }

    @Bean
    public PropertiesConfiguration clientConfig() {
        return new PropertiesConfiguration();
    }

    @Bean
    public ApiRegisterEventListener apiRegisterEventListener(GatewayClientRegisterRepository gatewayClientRegisterRepository) {
        return new ApiRegisterEventListener(gatewayClientRegisterRepository);
    }

    @Bean
    public RouteRegisterEventListener routeRegisterEventListener(GatewayClientRegisterRepository gatewayClientRegisterRepository) {
        return new RouteRegisterEventListener(gatewayClientRegisterRepository);
    }

    @Bean
    public GatewayClientRegisterRepository gatewayClientRegisterRepository(
                PropertiesConfiguration clientConfig,
                Environment env) {
        return new GatewayClientDubboRegisterRepository(clientConfig, env);
    }
}
