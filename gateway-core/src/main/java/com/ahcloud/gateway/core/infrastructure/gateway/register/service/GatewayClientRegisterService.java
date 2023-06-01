package com.ahcloud.gateway.core.infrastructure.gateway.register.service;

import com.ahcloud.gateway.register.common.dto.MetaDataRegisterDTO;
import com.ahcloud.gateway.register.common.dto.RouteRegisterDTO;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/5/29 10:24
 **/
public interface GatewayClientRegisterService {

    /**
     * 类型
     * @return
     */
    String rpcType();

    /**
     * 注册元数据
     *
     * @param metaDataRegisterDTO
     */
    void registerMetadata(MetaDataRegisterDTO metaDataRegisterDTO);

    /**
     * 注册路由
     *
     * @param name
     * @param registerDTO
     */
    void registerRoute(String name, RouteRegisterDTO registerDTO);
}
