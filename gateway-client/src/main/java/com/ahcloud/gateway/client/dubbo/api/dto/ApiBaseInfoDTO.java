package com.ahcloud.gateway.client.dubbo.api.dto;

import com.ahcloud.gateway.client.enums.ReadOrWriteEnum;
import lombok.*;

import java.io.Serializable;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/2/20 17:15
 **/
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ApiBaseInfoDTO implements Serializable {
    private static final long serialVersionUID = -548140650641738092L;

    /**
     * 接口编码
     */
    private String apiCode;

    /**
     * 接口名称
     */
    private String apiName;

    /**
     * 请求路径
     */
    private String apiPath;

    /**
     * 服务id
     */
    private String serviceId;

    /**
     * 全限定名
     */
    private String qualifiedName;

    /**
     * 方法名
     */
    private String methodName;

    /**
     * 接口类型
     */
    private Integer apiType;

    /**
     * 读写类型
     */
    private ReadOrWriteEnum readOrWrite;

    /**
     * 接口描述
     */
    private String apiDesc;

    /**
     * 接口状态(1:启用,2:停用)
     */
    private Integer status;

    /**
     * 是否认证
     */
    private Integer auth;

    /**
     * 是否可变
     */
    private Integer changeable;
}
