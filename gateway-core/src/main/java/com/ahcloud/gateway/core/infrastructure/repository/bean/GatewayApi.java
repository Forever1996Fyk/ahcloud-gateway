package com.ahcloud.gateway.core.infrastructure.repository.bean;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 网关接口表
 * </p>
 *
 * @author auto_generation
 * @since 2023-02-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("gateway_api")
public class GatewayApi implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
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
     * 全限定名
     */
    private String qualifiedName;

    /**
     * 方法名
     */
    private String methodName;

    /**
     * http方式
     */
    private String httpMethod;

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
    @TableField(value = "is_auth")
    private Integer auth;

    /**
     * 是否可变
     */
    private Integer changeable;

    /**
     * 开发环境状态
     */
    private Integer dev;

    /**
     * 联调环境状态
     */
    private Integer test;

    /**
     * 测试环境状态
     */
    private Integer sit;

    /**
     * 预发环境状态
     */
    private Integer pre;

    /**
     * 生产环境状态
     */
    private Integer prod;

    /**
     * 备注
     */
    private String remark;

    /**
     * 行记录创建者
     */
    private String creator;

    /**
     * 行记录最近更新人
     */
    private String modifier;

    /**
     * 行记录创建时间
     */
    private Date createdTime;

    /**
     * 行记录最近修改时间
     */
    private Date modifiedTime;

    /**
     * 行版本号
     */
    @Version
    private Integer version;

    /**
     * 拓展字段
     */
    private String extension;

    /**
     * 是否删除
     */
    private Long deleted;


}
