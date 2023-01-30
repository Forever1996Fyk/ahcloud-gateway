package com.ahcloud.gateway.server.infrastructure.util;

import com.ahcloud.gateway.client.AppPlatformEnum;
import com.ahcloud.gateway.server.application.constant.GatewayConstants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;

/**
 * @program: ahcloud-gateway
 * @description:  ServerWebExchange工具类
 * @author: YuKai Fan
 * @create: 2023/1/18 09:14
 **/
public class ServerWebExchangeUtils {

    /**
     * 从请求头获取原始 认证Token
     * @param exchange
     * @return
     */
    public static String getOriginTokenFromRequest(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        return request.getHeaders().getFirst(GatewayConstants.TOKEN_HEADER);
    }

    /**
     * 从请求头获取去除前缀 认证Token
     * @param exchange
     * @return
     */
    public static String getTokenFromRequest(ServerWebExchange exchange, String prefix) {
        String originToken = getOriginTokenFromRequest(exchange);
        // 如果以Bearer开头，则提取。
        if (StringUtils.startsWith(originToken, prefix.toLowerCase())) {
            String authHeaderValue = StringUtils.substring(originToken, prefix.length()).trim();
            int commaIndex = authHeaderValue.indexOf(',');
            if (commaIndex > 0) {
                authHeaderValue = authHeaderValue.substring(0, commaIndex);
            }
            return authHeaderValue;
        }
        return null;
    }

    /**
     * 从请求头获取app平台类型
     * @param exchange
     * @return
     */
    public static AppPlatformEnum getAppPlatformByRequest(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        String appPlatform = request.getHeaders().getFirst(GatewayConstants.APP_PLATFORM);
        return AppPlatformEnum.getByValue(appPlatform);
    }

}
