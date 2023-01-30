package com.ahcloud.gateway.server.infrastructure.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/1/16 11:35
 **/
@Data
@Component
@RefreshScope
public class GatewayAuthProperties {

    /**
     * 忽略token的url集合
     */
    @Value(value = "{gateway.auth.ignoreTokenUrlSet:[]}")
    private Set<String> ignoreTokenUrlSet;

    /**
     * 忽略鉴权的url集合
     */
    @Value(value = "{gateway.auth.ignoreAuthUrlSet:[]}")
    private Set<String> ignoreAuthUrlSet;
}
