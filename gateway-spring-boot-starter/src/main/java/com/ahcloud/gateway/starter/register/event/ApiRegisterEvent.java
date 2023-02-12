package com.ahcloud.gateway.starter.register.event;

import com.ahcloud.gateway.client.dubbo.api.dto.ApiRegisterDTO;
import lombok.Data;
import org.springframework.context.ApplicationEvent;

import java.util.List;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/2/3 11:31
 **/
public class ApiRegisterEvent extends ApplicationEvent {

    private static final long serialVersionUID = 2647104054701753899L;

    /**
     * api注册列表
     */
    private List<ApiRegisterDTO> apiRegisterList;

    public ApiRegisterEvent(List<ApiRegisterDTO> apiRegisterList) {
        super(apiRegisterList);
        this.apiRegisterList = apiRegisterList;
    }

    public List<ApiRegisterDTO> getApiRegisterList() {
        return apiRegisterList;
    }

    public void setApiRegisterList(List<ApiRegisterDTO> apiRegisterList) {
        this.apiRegisterList = apiRegisterList;
    }
}
