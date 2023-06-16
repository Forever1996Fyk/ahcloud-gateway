package com.ahcloud.gateway.common.sentinel.model.enums;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/6/15 10:02
 **/
public enum AuthorityStrategyEnum {

    /**
     * 白名单
     */
    WHITE(0, "白名单"),

    /**
     * 黑名单
     */
    BLACK(1, "黑名单"),
    ;

    private final int type;
    private final String desc;

    AuthorityStrategyEnum(int type, String desc) {
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
