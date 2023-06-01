package com.ahcloud.gateway.core.infrastructure.gateway.listener.nacos;

import com.ahcloud.common.utils.JsonUtils;
import com.ahcloud.gateway.client.constant.NacosPathConstants;
import com.ahcloud.gateway.core.infrastructure.exception.GatewayException;
import com.ahcloud.gateway.core.infrastructure.gateway.listener.AbstractListDataChangedListener;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.ConfigType;
import com.alibaba.nacos.api.exception.NacosException;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/2/6 16:22
 **/
@Slf4j
public class NacosDataChangeListener extends AbstractListDataChangedListener {

    private final ConfigService configService;
    private final Environment environment;

    public NacosDataChangeListener(final ConfigService configService, Environment environment) {
        super(new ChangeData(NacosPathConstants.ROUTE_DATA_ID, NacosPathConstants.API_DATA_ID));
        this.configService = configService;
        this.environment = environment;
    }

//    @Override
//    protected String getConfig(String dataId) {
//        String config;
//        try {
//            config = configService.getConfig(dataId, getEnv(), NacosPathConstants.DEFAULT_TIME_OUT);
//            return StringUtils.isNotBlank(config) ? config : NacosPathConstants.EMPTY_CONFIG_DEFAULT_VALUE;
//        } catch (NacosException e) {
//            log.error("Gate data from nacos error, reason is {}", Throwables.getStackTraceAsString(e));
//            throw new GatewayException(e.getMessage());
//        }
//    }

    @Override
    protected void publishConfig(String dataId, Object data) {
        try {
            configService.publishConfig(
                    dataId,
                    getEnv(),
                    JsonUtils.toJsonString(data),
                    ConfigType.JSON.getType()
            );
        } catch (NacosException e) {
            log.error("Publish data to nacos error, reason is {}", Throwables.getStackTraceAsString(e));
            throw new GatewayException(e.getMessage());
        }
    }

    /**
     * 暂时先默认分组, 后期考虑按照环境分组
     * @return
     */
    private String getEnv() {
//        return this.environment.getProperty(GatewayClientConstants.ENV);
        return NacosPathConstants.GROUP;
    }
}
