package com.ahcloud.gateway.server.dubbo;

import com.ahcloud.common.result.RpcResult;
import com.ahcloud.gateway.client.dubbo.api.dto.ApiRegisterDTO;
import com.ahcloud.gateway.core.application.manager.GatewayApiRegisterManager;
import com.ahcloud.gateway.core.domain.api.bo.ApiRegisterBO;
import com.ahcloud.gateway.dubbo.api.ApiRegisterDubboService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/2/3 20:35
 **/
@DubboService(version = "1.0.0", timeout = 6000)
public class ApiRegisterDubboServiceImpl implements ApiRegisterDubboService {
    @Resource
    private GatewayApiRegisterManager gatewayApiRegisterManager;
    @Override
    public RpcResult<Boolean> apiRegister(ApiRegisterDTO registerDTO) {
        Boolean result = gatewayApiRegisterManager.apiRegister(convertToBO(registerDTO));
        return RpcResult.ofSuccess(result);
    }

    @Override
    public RpcResult<Boolean> apiBatchRegister(List<ApiRegisterDTO> apiRegisterDTOList) {
        Boolean result = gatewayApiRegisterManager.batchApiRegister(convertToBO(apiRegisterDTOList));
        return RpcResult.ofSuccess(result);
    }

    @Override
    public RpcResult<Boolean> deleteApiRegisterByServiceId(String serviceId) {
        Integer row = gatewayApiRegisterManager.deleteApiRegisterByServiceId(serviceId);
        return RpcResult.ofSuccess(row > 0);
    }

    private ApiRegisterBO convertToBO(ApiRegisterDTO dto) {
        return ApiRegisterBO.builder()
                .apiPath(dto.getApiPath())
                .apiHttpMethodEnum(dto.getApiHttpMethodEnum())
                .consume(dto.getConsume())
                .produce(dto.getProduce())
                .methodName(dto.getMethodName())
                .qualifiedName(dto.getQualifiedName())
                .serviceId(dto.getServiceId())
                .build();
    }

    private List<ApiRegisterBO> convertToBO(List<ApiRegisterDTO> apiRegisterDTOList) {
        return apiRegisterDTOList.stream()
                .map(this::convertToBO)
                .collect(Collectors.toList());
    }
}
