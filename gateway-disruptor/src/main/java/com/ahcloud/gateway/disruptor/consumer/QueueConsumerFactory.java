package com.ahcloud.gateway.disruptor.consumer;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/5/26 09:52
 **/
public interface QueueConsumerFactory<T> {

    /**
     * 创建 队列消费者执行器
     * @return
     */
    QueueConsumerExecutor<T> create();

    /**
     * Fix Name
     * @return
     */
    String fixName();
}
