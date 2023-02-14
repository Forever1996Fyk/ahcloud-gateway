package com.ahcloud.gateway.starter.register.event;

import com.ahcloud.gateway.client.dubbo.api.dto.ApiRegisterDTO;
import com.ahcloud.gateway.client.dubbo.route.dto.RouteRegisterDTO;
import org.springframework.context.ApplicationEvent;

import java.util.List;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/2/3 11:31
 **/
public class RouteRegisterEvent extends ApplicationEvent {

    private static final long serialVersionUID = 4841296479045464559L;
    /**
     * 路由注册
     */
    private RouteRegisterDTO registerDTO;

    public RouteRegisterEvent(RouteRegisterDTO registerDTO) {
        super(registerDTO);
        this.registerDTO = registerDTO;
    }

    public RouteRegisterDTO getRegisterDTO() {
        return registerDTO;
    }

    public void setRegisterDTO(RouteRegisterDTO registerDTO) {
        this.registerDTO = registerDTO;
    }
}
