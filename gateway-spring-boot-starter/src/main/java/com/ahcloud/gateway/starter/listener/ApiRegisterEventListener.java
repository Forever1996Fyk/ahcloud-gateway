package com.ahcloud.gateway.starter.listener;

import com.ahcloud.gateway.client.dubbo.api.dto.ApiRegisterDTO;
import com.ahcloud.gateway.starter.register.event.ApiRegisterEvent;
import com.ahcloud.gateway.starter.repository.GatewayClientRegisterRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/2/3 11:40
 **/
@Slf4j
public class ApiRegisterEventListener implements ApplicationListener<ApiRegisterEvent> {

    private final GatewayClientRegisterRepository gatewayClientRegisterRepository;

    public ApiRegisterEventListener(GatewayClientRegisterRepository gatewayClientRegisterRepository) {
        this.gatewayClientRegisterRepository = gatewayClientRegisterRepository;
    }

    @Override
    public void onApplicationEvent(ApiRegisterEvent apiRegisterEvent) {
        List<ApiRegisterDTO> apiRegisterList = apiRegisterEvent.getApiRegisterList();
        if (CollectionUtils.isEmpty(apiRegisterList)) {
            return;
        }
        gatewayClientRegisterRepository.batchPersistApi(apiRegisterList);
    }
}
