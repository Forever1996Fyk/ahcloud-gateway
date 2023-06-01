package com.ahcloud.gateway.starter.disruptor.executor;

import com.ahcloud.gateway.disruptor.consumer.QueueConsumerExecutor;
import com.ahcloud.gateway.register.common.subsriber.AbstractQueueConsumerFactory;
import com.ahcloud.gateway.register.common.subsriber.ExecutorTypeSubscriber;
import com.ahcloud.gateway.register.common.type.DataType;
import com.ahcloud.gateway.register.common.type.DataTypeParent;
import com.google.common.collect.Lists;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/5/26 09:59
 **/
public class RegisterClientConsumerExecutor<T extends DataTypeParent> extends QueueConsumerExecutor<T> {

    private final Map<DataType, ExecutorTypeSubscriber<T>> subscribers;

    public RegisterClientConsumerExecutor(Map<DataType, ExecutorTypeSubscriber<T>> subscribers) {
        this.subscribers = subscribers;
    }

    @Override
    public void run() {
        final T data = getData();
        subscribers.get(data.getType()).executor(Lists.newArrayList(data));
    }

    public static class RegisterClientExecutorFactory<T extends DataTypeParent> extends AbstractQueueConsumerFactory<T> {

        @Override
        public QueueConsumerExecutor<T> create() {
            Map<DataType, ExecutorTypeSubscriber<T>> map = getSubscribers().stream()
                    .map(e -> (ExecutorTypeSubscriber<T>) e)
                    .collect(Collectors.toMap(ExecutorTypeSubscriber::getType, e -> e));
            return new RegisterClientConsumerExecutor<>(map);
        }

        @Override
        public String fixName() {
            return "gateway_register_client";
        }
    }
}
