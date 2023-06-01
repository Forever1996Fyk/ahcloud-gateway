package com.ahcloud.gateway.client.enums;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/5/25 16:49
 **/
public enum DefaultApiDefinitionEnum {

    /**
     * 默认api定义
     */
    ADMIN_LOGOUT("admin.uaa.logout", "/ahcloud-admin-api/logout", false)

    ;

    private final String apiCode;
    private final String path;
    private final boolean auth;

    DefaultApiDefinitionEnum(String apiCode, String path, boolean auth) {
        this.apiCode = apiCode;
        this.path = path;
        this.auth = auth;
    }

    public String getApiCode() {
        return apiCode;
    }

    public String getPath() {
        return path;
    }

    public boolean isAuth() {
        return auth;
    }

    public static Set<String> defaultApiCodeSet() {
        return Arrays.stream(values())
                .map(DefaultApiDefinitionEnum::getApiCode)
                .collect(Collectors.toSet());
    }
}
