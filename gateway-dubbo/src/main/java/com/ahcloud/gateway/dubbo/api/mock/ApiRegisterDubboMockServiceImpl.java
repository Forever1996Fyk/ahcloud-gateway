package com.ahcloud.gateway.dubbo.api.mock;

import com.ahcloud.common.result.RpcResult;
import com.ahcloud.gateway.client.dubbo.api.dto.ApiRegisterDTO;
import com.ahcloud.gateway.client.dubbo.route.dto.RouteRegisterDTO;
import com.ahcloud.gateway.dubbo.api.ApiRegisterDubboService;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/2/21 20:44
 **/
@Slf4j
public class ApiRegisterDubboMockServiceImpl implements ApiRegisterDubboService {
    @Override
    public RpcResult<Boolean> apiRegister(ApiRegisterDTO registerDTO) {
        log.error("GatewayServer Api Register service, method is apiRegister offline");
        return RpcResult.ofSuccess(false);
    }

    @Override
    public RpcResult<Boolean> apiBatchRegister(List<ApiRegisterDTO> apiRegisterDTOList) {
        log.error("GatewayServer Api Register service, method is apiBatchRegister offline");
        return RpcResult.ofSuccess(false);
    }

    @Override
    public RpcResult<Boolean> deleteApiRegisterByServiceId(String serviceId) {
        log.error("GatewayServer Api Register service, method is deleteApiRegisterByServiceId offline");
        return RpcResult.ofSuccess(false);
    }
}
