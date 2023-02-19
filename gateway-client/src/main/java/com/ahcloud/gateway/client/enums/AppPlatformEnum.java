package com.ahcloud.gateway.client.enums;

import java.util.Arrays;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/1/18 09:28
 **/
public enum AppPlatformEnum {

    /**
     * app平台类型
     */
    ADMIN("admin", "系统后台"),
    APP("app", "app"),
    ;

    private final String value;

    private final String desc;

    AppPlatformEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static AppPlatformEnum getByValue(String value) {
        return Arrays.stream(values())
                .filter(appPlatformEnum -> appPlatformEnum.getValue().equals(value))
                .findFirst()
                .orElse(null);
    }
}
