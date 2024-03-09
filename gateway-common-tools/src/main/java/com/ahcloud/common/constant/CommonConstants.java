package com.ahcloud.common.constant;

/**
 * @program: ahcloud-common
 * @description:
 * @author: YuKai Fan
 * @create: 2023/1/30 16:43
 **/
public class CommonConstants {

    /**
     * 手机号正则
     */
    public static final String PHONE_REGEX = "^(1[3-9]\\d{9}$)";

    /**
     * 邮箱正则
     */
    public static final String EMAIL_REGEX = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

    /**
     * 逗号分隔符
     */
    public static final String COMMA_SEPARATOR = ",";

    /**
     * 点分隔符
     */
    public static final String DOT_SEPARATOR = ".";

    /**
     * 冒号隔符
     */
    public static final String COLON_SEPARATOR = ":";


    /**
     * Long  0
     */
    public static final Long ZERO = 0L;

    /**
     * Integer 0
     */
    public static final Integer ZERO_INT = 0;
}
