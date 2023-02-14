package com.ahcloud.gateway.starter.listener;

import com.ahcloud.gateway.client.dubbo.api.dto.ApiRegisterDTO;
import com.ahcloud.gateway.client.dubbo.route.dto.RouteRegisterDTO;
import com.ahcloud.gateway.starter.register.event.ApiRegisterEvent;
import com.ahcloud.gateway.starter.register.event.RouteRegisterEvent;
import com.ahcloud.gateway.starter.repository.GatewayClientRegisterRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/2/3 11:40
 **/
@Slf4j
public class RouteRegisterEventListener implements ApplicationListener<RouteRegisterEvent> {

    private final GatewayClientRegisterRepository gatewayClientRegisterRepository;

    public RouteRegisterEventListener(GatewayClientRegisterRepository gatewayClientRegisterRepository) {
        this.gatewayClientRegisterRepository = gatewayClientRegisterRepository;
    }

    @Override
    public void onApplicationEvent(RouteRegisterEvent registerEvent) {
        RouteRegisterDTO registerDTO = registerEvent.getRegisterDTO();
        if (Objects.isNull(registerDTO)) {
            return;
        }
        gatewayClientRegisterRepository.persistRoute(registerDTO);
    }
}
