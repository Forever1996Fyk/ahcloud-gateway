package com.ahcloud.gateway.core.domain.api.vo;

import lombok.*;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/2/4 11:14
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiVO {
    /**
     * 主键id
     */
    private Long id;

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
     * 全限定名 + 方法名
     */
    private String className;

    /**
     * 接口状态(1:启用,2:停用)
     */
    private Integer status;

    /**
     * 是否认证
     */
    private Integer auth;
}
