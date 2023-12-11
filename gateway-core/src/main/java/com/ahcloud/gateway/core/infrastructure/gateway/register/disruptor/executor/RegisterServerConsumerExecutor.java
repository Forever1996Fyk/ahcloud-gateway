package com.ahcloud.gateway.core.infrastructure.gateway.register.disruptor.executor;

import com.ahcloud.common.utils.CollectionUtils;
import com.ahcloud.gateway.disruptor.consumer.QueueConsumerExecutor;
import com.ahcloud.gateway.disruptor.consumer.QueueConsumerFactory;
import com.ahcloud.gateway.register.common.dto.MetaDataRegisterDTO;
import com.ahcloud.gateway.register.common.dto.RouteRegisterDTO;
import com.ahcloud.gateway.register.common.subsriber.ExecutorSubscriber;
import com.ahcloud.gateway.register.common.subsriber.ExecutorTypeSubscriber;
import com.ahcloud.gateway.register.common.type.DataType;
import com.ahcloud.gateway.register.common.type.DataTypeParent;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/5/29 10:29
 **/
public class RegisterServerConsumerExecutor extends QueueConsumerExecutor<Collection<DataTypeParent>> {

    private final Map<DataType, ExecutorTypeSubscriber<DataTypeParent>> subscribers;

    public RegisterServerConsumerExecutor(Map<DataType, ExecutorTypeSubscriber<DataTypeParent>> subscribers) {
        this.subscribers = subscribers;
    }

    @Override
    public void run() {
        List<DataTypeParent> results = getData().stream()
                .filter(this::isValidData)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(results)) {
            return;
        }
        selectExecutor(results).executor(results);
    }

    private boolean isValidData(final Object data) {
        if (data instanceof RouteRegisterDTO) {
            RouteRegisterDTO routeRegisterDTO = (RouteRegisterDTO) data;
            return StringUtils.isNoneBlank(String.valueOf(routeRegisterDTO.getAppId()), routeRegisterDTO.getAppName(), routeRegisterDTO.getServiceId(), routeRegisterDTO.getRpcType());
        }
        if (data instanceof MetaDataRegisterDTO) {
            MetaDataRegisterDTO metaDataRegisterDTO = (MetaDataRegisterDTO) data;
            return StringUtils.isNoneBlank(
                    String.valueOf(metaDataRegisterDTO.getAppId()),
                    metaDataRegisterDTO.getAppName(),
                    metaDataRegisterDTO.getApiPath(),
                    metaDataRegisterDTO.getRpcType());
        }
        return true;
    }

    private ExecutorSubscriber<DataTypeParent> selectExecutor(final Collection<DataTypeParent> list) {
        final Optional<DataTypeParent> first = list.stream().findFirst();
        return subscribers.get(first.orElseThrow(() -> new RuntimeException("the data type is not found")).getType());
    }

    public static class RegisterServerExecutorFactory implements QueueConsumerFactory<Collection<DataTypeParent>> {
        /**
         * The Subscribers.
         */
        private final Set<ExecutorTypeSubscriber<? extends DataTypeParent>> subscribers = new HashSet<>();

        @Override
        public QueueConsumerExecutor<Collection<DataTypeParent>> create() {
            Map<DataType, ExecutorTypeSubscriber<DataTypeParent>> maps = getSubscribers()
                    .stream()
                    .map(e -> (ExecutorTypeSubscriber<DataTypeParent>) e)
                    .collect(Collectors.toMap(ExecutorTypeSubscriber::getType, e -> e));
            return new RegisterServerConsumerExecutor(maps);
        }

        @Override
        public String fixName() {
            return "gateway_register_server";
        }


        /**
         * Add subscribers abstract queue consumer factory.
         *
         * @param subscriber the subscriber
         * @return the abstract queue consumer factory
         */
        public RegisterServerExecutorFactory addSubscribers(final ExecutorTypeSubscriber<? extends DataTypeParent> subscriber) {
            subscribers.add(subscriber);
            return this;
        }

        /**
         * Gets subscribers.
         *
         * @return the subscribers
         */
        public Set<ExecutorTypeSubscriber<? extends DataTypeParent>> getSubscribers() {
            return subscribers;
        }
    }

}
