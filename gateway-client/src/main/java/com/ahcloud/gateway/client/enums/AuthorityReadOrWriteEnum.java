package com.ahcloud.gateway.client.enums;

import java.util.Objects;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/2/1 16:41
 **/
public enum AuthorityReadOrWriteEnum {
    /**
     * 未知
     */
    UNKNOWN(-1, "未知"),

    /**
     * 读
     */
    READ(1, "读"),

    /**
     * 写
     */
    WRITE(2, "写"),
    ;


    private final int type;

    private final String desc;

    AuthorityReadOrWriteEnum(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public int getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    public static boolean isValid(Integer type) {
        AuthorityReadOrWriteEnum readOrWriteEnum = getByType(type);
        return !Objects.equals(readOrWriteEnum, UNKNOWN);
    }


    public boolean isWrite() {
        return Objects.equals(this, WRITE);
    }


    public static AuthorityReadOrWriteEnum getByType(Integer type) {
        AuthorityReadOrWriteEnum[] values = values();
        for (AuthorityReadOrWriteEnum readOrWriteEnum : values) {
            if (Objects.equals(readOrWriteEnum.getType(), type)) {
                return readOrWriteEnum;
            }
        }
        return UNKNOWN;
    }
}
