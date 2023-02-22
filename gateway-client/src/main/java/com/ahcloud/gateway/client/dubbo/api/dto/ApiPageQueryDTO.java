package com.ahcloud.gateway.client.dubbo.api.dto;

import com.ahcloud.gateway.client.dubbo.PageQueryDTO;
import lombok.*;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/2/20 17:19
 **/
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ApiPageQueryDTO extends PageQueryDTO {
    private static final long serialVersionUID = -4974134579075064103L;

    /**
     * api编码
     */
    private String apiCode;

    /**
     * 接口名称
     */
    private String name;

    /**
     * 服务id
     */
    private String serviceId;
}
