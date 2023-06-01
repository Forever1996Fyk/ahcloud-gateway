package com.ahcloud.gateway.starter.repository;

import com.ahcloud.gateway.register.common.dto.MetaDataRegisterDTO;
import com.ahcloud.gateway.register.common.config.PropertiesConfiguration;
import com.ahcloud.gateway.client.dubbo.api.dto.ApiRegisterDTO;
import com.ahcloud.gateway.register.common.dto.RouteRegisterDTO;

import java.util.List;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/2/3 19:26
 **/
public interface GatewayClientRegisterRepository {

    /**
     * 初始化
     * @param propertiesConfiguration
     */
    default void init(PropertiesConfiguration propertiesConfiguration) {

    }

    /**
     * 持久化路由
     * @param registerDTO
     */
    void persistRoute(RouteRegisterDTO registerDTO);

    /**
     * 持久化api 元数据
     * @param registerDTO
     */
    void persistMetaData(MetaDataRegisterDTO registerDTO);

    /**
     * 关闭
     */
    default void close() {

    }
}
