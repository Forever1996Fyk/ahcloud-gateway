package com.ahcloud.gateway.core.domain.context;

import com.ahcloud.gateway.client.enums.AppPlatformEnum;
import com.ahcloud.gateway.core.domain.bo.ApiGatewayBO;
import com.ahcloud.gateway.core.domain.bo.UserInfoBO;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.PathContainer;

import java.util.Map;

/**
 * @program: ahcloud-gateway
 * @description: 网关上下文信息
 * @author: YuKai Fan
 * @create: 2023/1/16 08:54
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GatewayContext {
    public static final String CACHE_GATEWAY_CONTEXT = "cacheGatewayContext";

    /**
     * cache headers
     */
    private HttpHeaders headers;

    /**
     * ip 地址
     */
    private String ipAddress;

    /**
     * 请求源域名
     */
    private String origin;

    /**
     * 请求路径
     */
    private PathContainer pathContainer;

    /**
     * api网关信息
     */
    private ApiGatewayBO apiGatewayBO;

    /**
     * 用户信息
     */
    private UserInfoBO userInfoBO;

    /**
     * app-platform
     */
    private AppPlatformEnum appPlatformEnum;

    /**
     * 租户id
     */
    private String tenantId;

    /**
     * 属性
     */
    private Map<String, Object> attribute = Maps.newHashMap();
}
