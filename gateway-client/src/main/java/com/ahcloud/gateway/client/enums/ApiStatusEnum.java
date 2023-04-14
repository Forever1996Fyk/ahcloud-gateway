package com.ahcloud.gateway.client.enums;

import com.google.common.collect.Lists;
import lombok.Getter;

import java.util.List;
import java.util.Objects;

/**
 * @program: permissions-center
 * @description: api状态
 * @author: YuKai Fan
 * @create: 2021-12-24 15:29
 **/
@Getter
public enum ApiStatusEnum {

    /**
     * 未知
     */
    UNKNOWN(-1, "未知"),


    /**
     * 下线
     */
    OFFLINE(0, "下线"),

    /**
     * 上线
     */
    ONLINE(1, "正常"),

    /**
     * 禁用
     */
    DISABLED(2, "禁用"),
    ;

    /**
     * 状态
     */
    private final Integer status;

    /**
     * 描述
     */
    private final String desc;

    ApiStatusEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public static boolean isValid(Integer status) {
        ApiStatusEnum apiStatusEnum = valueOf(status);
        return !Objects.equals(apiStatusEnum, UNKNOWN);
    }

    /**
     * 根据value找到对应的枚举
     *
     * @param value value值
     * @return 枚举值
     */
    public static ApiStatusEnum valueOf(Integer value) {
        for (ApiStatusEnum e : ApiStatusEnum.values()) {
            if (Objects.equals(e.status, value)) {
                return e;
            }
        }
        return UNKNOWN;
    }

    private final static List<ApiStatusEnum> DISABLED_LIST = Lists.newArrayList(
            UNKNOWN,
            DISABLED,
            OFFLINE
    );
    public boolean disabled() {
        return DISABLED_LIST.contains(this);
    }

    public boolean enabled() {
        return Objects.equals(this, ONLINE);
    }
}
