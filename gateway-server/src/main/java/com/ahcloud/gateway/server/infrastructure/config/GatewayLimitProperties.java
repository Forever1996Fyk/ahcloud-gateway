package com.ahcloud.gateway.server.infrastructure.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/6/16 17:26
 **/
@Data
@RefreshScope
@Configuration
public class GatewayLimitProperties {

    /**
     * 时间段(单位：毫秒)
     */
    @Value("${gateway.limit.black.period:1}")
    private int period;
    /**
     * 次数
     */
    @Value("${gateway.limit.black.count:10}")
    private int count;
}
