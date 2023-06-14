package com.ahcloud.gateway.api.controller;

import com.ahcloud.common.result.ResponseResult;
import com.ahcloud.gateway.core.application.manager.GatewayApiMetadataManager;
import com.ahcloud.gateway.core.domain.api.vo.ApiMetadataVO;
import com.ahcloud.gateway.core.domain.api.vo.ServiceIdSelectVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/5/29 17:24
 **/
@RestController
@RequestMapping("/metadata")
public class GatewayApiMetaDataController {
    @Resource
    private GatewayApiMetadataManager gatewayApiMetadataManager;

    /**
     * 获取服务id选择列表
     * @param name
     * @return
     */
    @GetMapping("/selectServiceIdList")
    public ResponseResult<List<ServiceIdSelectVO>> selectServiceIdList(String name) {
        return ResponseResult.ofSuccess(gatewayApiMetadataManager.listServiceIdByName(name));
    }

    /**
     * 根据服务id获取api元数据选择列表
     * @param appName
     * @param env
     * @return
     */
    @GetMapping("/selectApiMetadataList")
    public ResponseResult<List<ApiMetadataVO>> selectApiMetadataList(String appName, String env) {
        List<ApiMetadataVO> list = gatewayApiMetadataManager.selectApiMetadataList(appName, env);
        return ResponseResult.ofSuccess(list);
    }
}
