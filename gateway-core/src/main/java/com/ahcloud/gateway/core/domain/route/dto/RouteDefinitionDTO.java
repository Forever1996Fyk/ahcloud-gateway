package com.ahcloud.gateway.core.domain.route.dto;

import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/2/21 23:20
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RouteDefinitionDTO {

    /**
     * 路由id
     */
    private String id;

    /**
     * 应用名
     */
    private String appName;

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
     * 当前环境
     */
    private String env;

    /**
     * 断言
     */
    private List<PredicateDefinitionDTO> predicates;

    /**
     * 过滤器
     */
    private List<FilterDefinitionDTO> filters;

    /**
     * uri
     */
    private URI uri;

    /**
     * 元数据
     */
    private Map<String, Object> metadata;

    /**
     * 序号
     */
    private Integer order;
}
