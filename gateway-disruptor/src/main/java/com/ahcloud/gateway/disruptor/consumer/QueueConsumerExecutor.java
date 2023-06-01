package com.ahcloud.gateway.disruptor.consumer;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/5/26 09:51
 **/
public abstract class QueueConsumerExecutor<T> implements Runnable {

    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
