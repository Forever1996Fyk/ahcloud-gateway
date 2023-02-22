package com.ahcloud.gateway.core.domain.api.form;

import lombok.Data;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/2/4 10:37
 **/
@Data
public class ApiUpdateForm {

    /**
     * 唯一主键
     */
    private Long id;

    /**
     * 接口名称
     */
    private String apiName;

    /**
     * 请求路径
     */
    private String apiPath;

    /**
     * 接口类型
     */
    private Integer apiType;

    /**
     * 读写类型
     */
    private Integer readOrWrite;

    /**
     * 接口描述
     */
    private String apiDesc;

    /**
     * 是否认证
     */
    private Integer isAuth;
}
