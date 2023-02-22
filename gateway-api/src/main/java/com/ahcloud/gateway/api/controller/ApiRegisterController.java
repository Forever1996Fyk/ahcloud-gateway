package com.ahcloud.gateway.api.controller;

import com.ahcloud.common.result.ResponseResult;
import com.ahcloud.gateway.core.application.manager.GatewayApiRegisterManager;
import com.ahcloud.gateway.core.domain.api.vo.ApiRegisterVO;
import com.ahcloud.gateway.core.domain.response.GatewayResponseResult;
import com.ahcloud.gateway.starter.annotation.GatewaySpringCloudClient;
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
@GatewaySpringCloudClient
public class ApiRegisterController {
    @Resource
    private GatewayApiRegisterManager gatewayApiRegisterManager;

    /**
     * 获取服务id选择列表
     * @param name
     * @return
     */
    @GetMapping("/selectServiceIdList")
    @GatewaySpringCloudClient
    public ResponseResult<List<String>> selectServiceIdList(String name) {
        List<String> list = gatewayApiRegisterManager.listServiceIdByName(name);
        return ResponseResult.ofSuccess(list);
    }

    /**
     * 根据服务id获取api注册选择列表
     * @param serviceId
     * @param name
     * @return
     */
    @GetMapping("/selectApiRegisterList")
    @GatewaySpringCloudClient
    public ResponseResult<List<ApiRegisterVO>> selectApiRegisterList(String serviceId, String name) {
        List<ApiRegisterVO> list = gatewayApiRegisterManager.listApiRegisterByServiceId(serviceId, name);
        return ResponseResult.ofSuccess(list);
    }

}
