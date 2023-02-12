package com.ahcloud.gateway.starter.repository;

import com.ahcloud.common.result.RpcResult;
import com.ahcloud.common.utils.JsonUtils;
import com.ahcloud.gateway.client.dubbo.api.dto.ApiRegisterDTO;
import com.ahcloud.gateway.dubbo.api.ApiRegisterDubboService;
import com.ahcloud.gateway.starter.configuration.PropertiesConfiguration;
import com.ahcloud.gateway.starter.shutdown.GatewayClientShutdownHook;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/2/3 19:30
 **/
@Slf4j
public class GatewayClientDubboRegisterRepository implements GatewayClientRegisterRepository, InitializingBean {

    @DubboReference(version = "1.0.0", timeout = 6000)
    private ApiRegisterDubboService apiRegisterDubboService;

    private final PropertiesConfiguration clientConfig;
    private final Environment env;

    public GatewayClientDubboRegisterRepository(PropertiesConfiguration clientConfig, Environment env) {
        this.clientConfig = clientConfig;
        this.env = env;
    }

    @Override
    public void persistApi(ApiRegisterDTO apiRegisterDTO) {
        try {
            RpcResult<Boolean> result = apiRegisterDubboService.apiRegister(apiRegisterDTO);
            if (result.isFailed() || !result.getData()) {
                log.error("GatewayClientDubboRegisterRepository[persistApi] client register error is {}", apiRegisterDTO);
            }
        } catch (Exception e) {
            log.error("GatewayClientDubboRegisterRepository[persistApi] client register error is {}, cause by {}"
                    , apiRegisterDTO
                    , Throwables.getStackTraceAsString(e));
        }
    }

    @Override
    public void batchPersistApi(List<ApiRegisterDTO> apiRegisterDTOList) {
        try {
            RpcResult<Boolean> result = apiRegisterDubboService.apiBatchRegister(apiRegisterDTOList);
            if (result.isFailed() || !result.getData()) {
                log.error("GatewayClientDubboRegisterRepository[batchPersistApi] client register error is {}", JsonUtils.toJsonString(apiRegisterDTOList));
            }
        } catch (Exception e) {
            log.error("GatewayClientDubboRegisterRepository[batchPersistApi] client register error is {}, cause by {}"
                    , JsonUtils.toJsonString(apiRegisterDTOList)
                    , Throwables.getStackTraceAsString(e));
        }
    }

    @Override
    public void close() {
        String serviceId = getServiceId();
        try {
            RpcResult<Boolean> result = apiRegisterDubboService.deleteApiRegisterByServiceId(serviceId);
            if (result.isFailed() || !result.getData()) {
                log.error("GatewayClientDubboRegisterRepository[close] client register error is {}", serviceId);
            }
        } catch (Exception e) {
            log.error("GatewayClientDubboRegisterRepository[close] client register error is {}, cause by {}"
                    , serviceId
                    , Throwables.getStackTraceAsString(e));
        }
    }

    private String getServiceId() {
        return env.getProperty("spring.application.name");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        GatewayClientShutdownHook.set(this, clientConfig.getProps());
    }
}