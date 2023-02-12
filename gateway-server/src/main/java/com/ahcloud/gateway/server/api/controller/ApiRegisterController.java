package com.ahcloud.gateway.server.api.controller;

import com.ahcloud.gateway.server.application.manager.GatewayApiRegisterManager;
import com.ahcloud.gateway.server.domain.api.vo.ApiRegisterVO;
import com.ahcloud.gateway.server.domain.response.GatewayResponseResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/2/4 10:05
 **/
@RestController
@RequestMapping("/apiRegister")
public class ApiRegisterController {
    @Resource
    private GatewayApiRegisterManager gatewayApiRegisterManager;

    /**
     * 获取服务id选择列表
     * @param name
     * @return
     */
    @GetMapping("/selectServiceIdList")
    public GatewayResponseResult selectServiceIdList(String name) {
        List<String> list = gatewayApiRegisterManager.listServiceIdByName(name);
        return GatewayResponseResult.ofSuccess(list);
    }

    /**
     * 根据服务id获取api注册选择列表
     * @param serviceId
     * @param name
     * @return
     */
    @GetMapping("/selectApiRegisterList")
    public GatewayResponseResult selectApiRegisterList(String serviceId, String name) {
        List<ApiRegisterVO> list = gatewayApiRegisterManager.listApiRegisterByServiceId(serviceId, name);
        return GatewayResponseResult.ofSuccess(list);
    }

}
