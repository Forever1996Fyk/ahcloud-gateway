package com.ahcloud.gateway.dubbo.api;

import com.ahcloud.common.result.RpcResult;
import com.ahcloud.gateway.client.dubbo.api.dto.ApiRegisterDTO;

import java.util.List;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/2/3 19:33
 **/
public interface ApiRegisterDubboService {

    /**
     * 接口注册
     * @param registerDTO
     * @param env
     * @return
     */
    RpcResult<Boolean> apiRegister(ApiRegisterDTO registerDTO, String env);

    /**
     * 批量接口注册
     * @param apiRegisterDTOList
     * @param env
     * @return
     */
    RpcResult<Boolean> apiBatchRegister(List<ApiRegisterDTO> apiRegisterDTOList, String env);

    /**
     * 根据服务id清除接口
     * @param serviceId
     * @param env
     * @return
     */
    RpcResult<Boolean> deleteApiRegisterByServiceId(String serviceId, String env);
}
