package com.ahcloud.gateway.client.dubbo.api.dto;

import com.ahcloud.gateway.client.enums.ApiHttpMethodEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/2/3 11:32
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiRegisterDTO implements Serializable {
    private static final long serialVersionUID = -2064793389636053434L;

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
}
