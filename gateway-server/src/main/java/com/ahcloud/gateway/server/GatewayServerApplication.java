package com.ahcloud.gateway.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2022/12/6 17:30
 **/
@EnableDiscoveryClient
@MapperScan(basePackages = {"com.ahcloud.gateway.core.infrastructure.repository.mapper"})
@SpringBootApplication(scanBasePackages = {"com.ahcloud"})
public class GatewayServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayServerApplication.class, args);
    }
}
