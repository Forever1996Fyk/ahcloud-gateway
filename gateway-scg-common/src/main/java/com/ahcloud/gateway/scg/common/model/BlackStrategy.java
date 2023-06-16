package com.ahcloud.gateway.scg.common.model;

import com.ahcloud.gateway.scg.common.model.limit.RedisLimit;
import lombok.Data;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/6/16 11:15
 **/
@Data
public class BlackStrategy implements RedisLimit {

    /**
     * 资源名称
     */
    private String name;

    /**
     * 资源key
     */
    private String key;

    /**
     * 资源key前缀
     */
    private String prefix;

    /**
     * 时间段(单位秒)
     */
    private int period;

    /**
     * 次数
     */
    private int count;
}
