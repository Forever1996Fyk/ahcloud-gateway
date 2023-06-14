package com.ahcloud.gateway.client.enums;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/6/13 16:33
 **/
public enum RouteTypeEnum {

    /**
     * 系统
     */
    SYSTEM(1, "系统"),

    /**
     * 服务
     */
    SERVICE(2, "服务"),
    ;

    private final int type;
    private final String desc;

    RouteTypeEnum(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public int getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }
}
