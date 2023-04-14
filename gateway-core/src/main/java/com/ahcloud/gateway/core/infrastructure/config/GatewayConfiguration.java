package com.ahcloud.gateway.core.infrastructure.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/4/14 11:07
 **/
@Data
@Configuration
public class GatewayConfiguration {

    /**
     * 当前环境
     */
    @Value("${spring.profiles.active}")
    private String env;
}
