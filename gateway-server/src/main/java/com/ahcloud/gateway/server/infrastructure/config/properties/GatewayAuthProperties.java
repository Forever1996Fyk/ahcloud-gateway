package com.ahcloud.gateway.server.infrastructure.config.properties;

import com.google.common.collect.Sets;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
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
@ConfigurationProperties(prefix = "gateway.auth")
public class GatewayAuthProperties {

    /**
     * 忽略token的url集合
     */
    private String ignoreTokenUrlSet;

    /**
     * 忽略鉴权的url集合
     */
    private Set<String> ignoreAuthUrlSet;

}
