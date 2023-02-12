package com.ahcloud.gateway.starter.repository;

import com.ahcloud.gateway.starter.configuration.PropertiesConfiguration;
import com.ahcloud.gateway.client.dubbo.api.dto.ApiRegisterDTO;

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
     * 持久化接口
     * @param apiRegisterDTO
     */
    void persistApi(ApiRegisterDTO apiRegisterDTO);

    /**
     * 批量持久化接口
     * @param apiRegisterDTOList
     */
    void batchPersistApi(List<ApiRegisterDTO> apiRegisterDTOList);

    /**
     * 关闭
     */
    default void close() {

    }
}
