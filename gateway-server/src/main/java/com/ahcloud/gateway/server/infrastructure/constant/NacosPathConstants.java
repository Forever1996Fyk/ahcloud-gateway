package com.ahcloud.gateway.server.infrastructure.constant;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/2/6 16:23
 **/
public class NacosPathConstants {
    /**
     * Nacos config default group.
     */
    public static final String GROUP = "DEFAULT_GROUP";

    /**
     * route data id.
     */
    public static final String ROUTE_DATA_ID = "gateway.route.json";

    /**
     * default time out of get config.
     */
    public static final long DEFAULT_TIME_OUT = 6000;

    /**
     * default value of get config.
     */
    public static final String EMPTY_CONFIG_DEFAULT_VALUE = "{}";
}
