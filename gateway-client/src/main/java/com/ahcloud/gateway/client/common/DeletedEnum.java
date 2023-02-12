package com.ahcloud.gateway.client.common;

/**
 * @program: permissions-center
 * @description: 逻辑删除标识
 * @author: YuKai Fan
 * @create: 2021-12-06 16:53
 **/
public enum DeletedEnum {

    /**
     * 正常, 未删除
     */
    NO(0L, "正常");

    /**
     * value
     */
    public final long value;

    /**
     * 描述
     */
    public final String description;

    /**
     * 构造函数
     *
     * @param value       value值
     * @param description 描述
     */
    DeletedEnum(long value, String description) {
        this.value = value;
        this.description = description;
    }

    /**
     * 判断是否有效
     *
     * @param value value
     * @return true or false
     */
    public static boolean valid(long value) {
        for (DeletedEnum e : DeletedEnum.values()) {
            if (e.value == value) {
                return true;
            }
        }
        return false;
    }

    /**
     * 根据value找到对应的枚举
     *
     * @param value value值
     * @return 枚举值
     */
    public static DeletedEnum valueOf(long value) {
        for (DeletedEnum e : DeletedEnum.values()) {
            if (e.value == value) {
                return e;
            }
        }
        return null;
    }
}
