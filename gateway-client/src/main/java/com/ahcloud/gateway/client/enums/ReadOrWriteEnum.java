package com.ahcloud.gateway.client.enums;

import java.util.Objects;

/**
 * @program: permissions-center
 * @description:
 * @author: YuKai Fan
 * @create: 2022-04-03 14:53
 **/
public enum ReadOrWriteEnum {

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

    ReadOrWriteEnum(int type, String desc) {
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
        ReadOrWriteEnum readOrWriteEnum = getByType(type);
        return !Objects.equals(readOrWriteEnum, UNKNOWN);
    }


    public boolean isWrite() {
        return Objects.equals(this, WRITE);
    }


    public static ReadOrWriteEnum getByType(Integer type) {
        ReadOrWriteEnum[] values = values();
        for (ReadOrWriteEnum readOrWriteEnum : values) {
            if (Objects.equals(readOrWriteEnum.getType(), type)) {
                return readOrWriteEnum;
            }
        }
        return UNKNOWN;
    }
}
