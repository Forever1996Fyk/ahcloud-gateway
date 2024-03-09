package com.ahcloud.gateway.scg.common.config;

import com.ahcloud.gateway.scg.common.aware.GatewayApplicationContextAware;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @program: ahcloud-common
 * @description:
 * @author: YuKai Fan
 * @create: 2023/8/10 08:53
 **/
@Configuration
public class SpringExtAutoConfiguration {

    /**
     * Application context aware application context aware.
     *
     * @return the application context aware
     */
    @Bean
    public ApplicationContextAware applicationContextAware() {
        return new GatewayApplicationContextAware();
    }
}
