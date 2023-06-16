package com.ahcloud.gateway.scg.common.model.limit;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/6/16 11:16
 **/
public interface RedisLimit {

    /**
     * 资源名称
     * @return
     */
    String getName();

    /**
     * 资源key
     * @return
     */
    String getKey();

    /**
     * key前缀
     * @return
     */
    String getPrefix();

    /**
     * 给定时间段
     * @return
     */
    int getPeriod();

    /**
     * 最多访问次数
     * @return
     */
    int getCount();
}
