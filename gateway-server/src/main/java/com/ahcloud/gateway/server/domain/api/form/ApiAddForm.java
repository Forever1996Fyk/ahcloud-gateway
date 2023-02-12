package com.ahcloud.gateway.server.domain.api.form;

import com.ahcloud.common.annotation.EnumValid;
import com.ahcloud.gateway.client.enums.ApiTypeEnum;
import com.ahcloud.gateway.client.enums.ReadOrWriteEnum;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/2/4 10:37
 **/
@Data
public class ApiAddForm {

    /**
     * 接口编码
     */
    @NotEmpty(message = "接口编码不能为空")
    private String apiCode;

    /**
     * 接口名称
     */
    @NotEmpty(message = "接口名称不能为空")
    private String apiName;

    /**
     * 请求路径
     */
    @NotEmpty(message = "请求路径不能为空")
    private String apiPath;

    /**
     * 服务id
     */
    @NotEmpty(message = "服务id不能为空")
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
    @EnumValid(enumClass = ApiTypeEnum.class, enumMethod = "isValid")
    private Integer apiType;

    /**
     * 读写类型
     */
    @EnumValid(enumClass = ReadOrWriteEnum.class, enumMethod = "isValid")
    private Integer readOrWrite;

    /**
     * 接口描述
     */
    private String apiDesc;

    /**
     * 是否认证
     */
    private Integer auth;

    /**
     * 是否可变
     */
    private Integer changeable;
}
