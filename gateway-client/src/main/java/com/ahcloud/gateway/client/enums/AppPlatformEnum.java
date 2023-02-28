package com.ahcloud.gateway.client.enums;

import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.List;

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

    private final static List<AppPlatformEnum> NEED_TENANT_LIST = Lists.newArrayList(
            APP
    );
    public boolean needTenant() {
        return NEED_TENANT_LIST.contains(this);
    }
}
