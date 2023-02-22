package com.ahcloud.gateway.core.application.service;


import com.ahcloud.gateway.core.infrastructure.repository.bean.GatewayApiRegister;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 网关接口注册表 服务类
 * </p>
 *
 * @author auto_generation
 * @since 2023-02-03
 */
public interface GatewayApiRegisterService extends IService<GatewayApiRegister> {

    /**
     * 删除已经注册的api
     * @param serviceId
     * @param path
     * @param httpMethod
     * @return
     */
    int deleteApiRegister(String serviceId, String path, String httpMethod);

    /**
     * 删除已经注册的api
     * @param serviceId
     * @return
     */
    int deleteApiRegisterByServiceId(String serviceId);

}
