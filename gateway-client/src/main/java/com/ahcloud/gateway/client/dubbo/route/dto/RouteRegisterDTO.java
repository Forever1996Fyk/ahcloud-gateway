package com.ahcloud.gateway.client.dubbo.route.dto;

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
public class RouteRegisterDTO implements Serializable {
    private static final long serialVersionUID = -83990835873533903L;

    /**
     * 服务id(默认为项目名称spring.application.name)
     */
    private String serviceId;
}
