package com.ahcloud.gateway.client.constant;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/5/26 11:06
 **/
public class RegisterPathConstants {
    /**
     * root path of zookeeper register center.
     */
    public static final String ROOT_PATH = "/gateway/register";

    /**
     * constants of separator.
     */
    private static final String SEPARATOR = "/";


    /**
     * Dot separator.
     */
    private static final String DOT_SEPARATOR = ".";

    /**
     * 构造服务配置路径
     * @param rpcType
     * @param contextPath
     * @return
     */
    public static String buildServiceConfigPath(final String rpcType, final String contextPath) {
        final String serviceConfigPathOrigin = String.join(SEPARATOR, ROOT_PATH, "service", rpcType, contextPath)
                .replace("/", DOT_SEPARATOR).replace("*", "");
        final String serviceConfigPathAfterSubstring = serviceConfigPathOrigin.substring(1);
        if (serviceConfigPathAfterSubstring.endsWith(".")) {
            return serviceConfigPathAfterSubstring.substring(0, serviceConfigPathAfterSubstring.length() - 1);
        }
        return serviceConfigPathAfterSubstring;
    }

    /**
     * 构建路由服务配置名称
     * @param rpcType
     * @param contextPath
     * @param env
     * @return
     */
    public static String buildRouteServiceConfigName(final String rpcType, final String contextPath, final String env) {
        final String serviceConfigPathOrigin = String.join(SEPARATOR, ROOT_PATH, "route", env, rpcType, contextPath)
                .replace("/", DOT_SEPARATOR).replace("*", "");
        final String serviceConfigPathAfterSubstring = serviceConfigPathOrigin.substring(1);
        if (serviceConfigPathAfterSubstring.endsWith(".")) {
            return serviceConfigPathAfterSubstring.substring(0, serviceConfigPathAfterSubstring.length() - 1);
        }
        return serviceConfigPathAfterSubstring;
    }

    /**
     * Build nacos instance service path string.
     * build child path of "shenyu.register.service.{rpcType}".
     *
     * @param rpcType the rpc type
     * @return the string
     */
    public static String buildServiceInstancePath(final String rpcType) {
        return String.join(SEPARATOR, ROOT_PATH, "service", rpcType)
                .replace("/", DOT_SEPARATOR).substring(1);
    }
}
