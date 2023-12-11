package com.ahcloud.gateway.register.common.dto;

import com.ahcloud.gateway.register.common.type.DataType;
import com.ahcloud.gateway.register.common.type.DataTypeParent;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/2/12 21:46
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RouteRegisterDTO implements DataTypeParent {

    /**
     * 当前应用id
     */
    private Long appId;

    /**
     * 服务id(默认为项目名称spring.application.name)
     */
    private String serviceId;

    /**
     * 应用名
     */
    private String appName;

    /**
     * 类型
     */
    private String rpcType;

    /**
     * 上下文路径
     */
    private String contextPath;

    /**
     * 环境变量
     */
    private String env;

    /**
     * ip 地址
     */
    private String host;

    /**
     * 端口号
     */
    private Integer port;


    @Override
    @JsonIgnore
    public DataType getType() {
        return DataType.ROUTE;
    }
}
