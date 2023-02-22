package com.ahcloud.gateway.core.infrastructure.config;

import com.ahcloud.gateway.core.infrastructure.gateway.listener.DataChangeListener;
import com.ahcloud.gateway.core.infrastructure.gateway.listener.nacos.NacosDataChangeListener;
import com.alibaba.cloud.nacos.NacosConfigManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/2/6 22:52
 **/
@Slf4j
@Configuration
@ConditionalOnClass
public class NacosDataChangeConfiguration {

    /**
     * Data changed listener data changed listener.
     * @param configManager
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(NacosDataChangeListener.class)
    public DataChangeListener nacosDataChangeListener(final NacosConfigManager configManager, final Environment env) {
        return new NacosDataChangeListener(configManager.getConfigService(), env);
    }
}
