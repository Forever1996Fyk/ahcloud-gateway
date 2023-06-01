package com.ahcloud.gateway.register.common.subsriber;

import com.ahcloud.gateway.disruptor.consumer.QueueConsumerFactory;
import com.ahcloud.gateway.register.common.type.DataTypeParent;
import com.google.common.collect.Sets;

import java.util.Set;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/5/26 10:08
 **/
public abstract class AbstractQueueConsumerFactory<T extends DataTypeParent> implements QueueConsumerFactory<T> {
    /**
     * 订阅者集合
     */
    private final Set<ExecutorSubscriber<T>> subscribers = Sets.newHashSet();

    public AbstractQueueConsumerFactory<T> addSubscribers(final ExecutorSubscriber<T> subscriber) {
        subscribers.add(subscriber);
        return this;
    }

    public Set<ExecutorSubscriber<T>> getSubscribers() {
        return subscribers;
    }
}
