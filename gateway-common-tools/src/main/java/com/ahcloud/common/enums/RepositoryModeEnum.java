package com.ahcloud.common.enums;

/**
 * @program: permissions-center
 * @description:
 * @author: YuKai Fan
 * @create: 2022-01-19 15:47
 **/
public enum RepositoryModeEnum {

    /**
     * redis存储
     */
    REDIS(1, "redis存储"),

    /**
     * redis存储
     */
    IN_MEMORY(2, "内存存储"),

    /**
     * redis存储
     */
    MEMCACHED(3, "Memcached"),

    /**
     * redis存储
     */
    MONGODB(4, "mongoDB存储"),

    /**
     * redis存储
     */
    DB(5, "数据库存储"),
    ;

    private final Integer type;

    private final String  desc;

    RepositoryModeEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public Integer getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }
}
