package com.ahcloud.gateway.register.common.dto;

import com.ahcloud.gateway.client.enums.ApiHttpMethodEnum;
import com.ahcloud.gateway.register.common.type.DataType;
import com.ahcloud.gateway.register.common.type.DataTypeParent;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/5/26 10:26
 **/
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MetaDataRegisterDTO implements DataTypeParent {

    /**
     * 应用id
     */
    private Long appId;

    /**
     * 应用名
     */
    private String appName;

    /**
     * 环境变量
     */
    private String env;

    /**
     * 上下文路径
     */
    private String contextPath;

    /**
     * 类型
     */
    private String rpcType;

    /**
     * RequestMapping consume
     */
    private String consume;

    /**
     * RequestMapping produce
     */
    private String produce;

    /**
     * 请求路径
     */
    private String apiPath;

    /**
     * 服务id
     */
    private String serviceId;

    /**
     * http method
     */
    private ApiHttpMethodEnum apiHttpMethodEnum;

    /**
     * 全限定名
     */
    private String qualifiedName;

    /**
     * 方法名
     */
    private String methodName;

    @Override
    @JsonIgnore
    public DataType getType() {
        return DataType.META_DATA;
    }
}
