package com.ahcloud.gateway.core.infrastructure.repository.bean;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 网关路由定义表
 * </p>
 *
 * @author auto_generation
 * @since 2023-05-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("gateway_route_definition")
public class GatewayRouteDefinition implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * appId
     */
    private Long appId;

    /**
     * app名称
     */
    private String appName;

    /**
     * 路由id
     */
    private String routeId;

    /**
     * 服务id
     */
    private String serviceId;

    /**
     * rpc类型
     */
    private String rpcType;

    /**
     * 上下文路径
     */
    private String contextPath;

    /**
     * 路由路径
     */
    private String uri;

    /**
     * 断言定义配置
     */
    private String predicateDefinitionConfig;

    /**
     * 过滤器定义配置
     */
    private String filterDefinitionConfig;

    /**
     * 路由类型
     */
    private Integer routeType;

    /**
     * 环境变量
     */
    private String env;

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
