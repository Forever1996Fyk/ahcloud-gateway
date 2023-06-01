package com.ahcloud.gateway.client.util;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/5/26 11:38
 **/
public class UriUtils {

    private static final String PRE_FIX = "/";

    /**
     * Repair data string.
     *
     * @param name the name
     * @return the string
     */
    public static String repairData(final String name) {
        return name.startsWith(PRE_FIX) ? name : PRE_FIX + name;
    }
    /**
     * Remove prefix string.
     *
     * @param name the name
     * @return the string
     */
    public static String removePrefix(final String name) {
        return name.startsWith(PRE_FIX) ? name.substring(1) : name;
    }

}
