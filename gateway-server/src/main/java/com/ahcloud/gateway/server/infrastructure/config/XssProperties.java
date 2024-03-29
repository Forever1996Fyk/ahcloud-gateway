package com.ahcloud.gateway.server.infrastructure.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.List;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/4/26 11:26
 **/
@Data
@Configuration
@ConfigurationProperties(prefix = "xss")
public class XssProperties {

    /**
     * 是否开启，默认为 true
     */
    private boolean enable = true;

    /**
     * 需要排除的 URL，默认为空
     */
    private List<String> excludeUrls = Collections.emptyList();
}
