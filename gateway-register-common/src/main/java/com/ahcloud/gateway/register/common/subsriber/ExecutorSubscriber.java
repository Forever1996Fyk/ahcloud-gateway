package com.ahcloud.gateway.register.common.subsriber;

import java.util.Collection;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/5/26 10:07
 **/
public interface ExecutorSubscriber<T> {

    /**
     * 执行数据集合
     * @param dataList
     */
    void executor(Collection<T> dataList);
}
