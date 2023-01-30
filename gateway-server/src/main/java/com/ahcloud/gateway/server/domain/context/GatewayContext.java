package com.ahcloud.gateway.server.domain.context;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

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
     * 缓存的请求体(json类型)
     */
    private String cacheBody;

    /**
     * formData form表单数据
     */
    private MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();

    /**
     * ip 地址
     */
    private String ipAddress;

    /**
     * 请求路径
     */
    private String path;

}
