package com.ahcloud.gateway.core.infrastructure.gateway.register.disruptor.publisher;

import com.ahcloud.gateway.core.infrastructure.gateway.register.disruptor.executor.RegisterServerConsumerExecutor;
import com.ahcloud.gateway.core.infrastructure.gateway.register.disruptor.subscriber.MetadataExecutorSubscriber;
import com.ahcloud.gateway.core.infrastructure.gateway.register.disruptor.subscriber.RouteRegisterExecutorSubscriber;
import com.ahcloud.gateway.core.infrastructure.gateway.register.service.GatewayClientRegisterService;
import com.ahcloud.gateway.disruptor.DisruptorProviderManager;
import com.ahcloud.gateway.disruptor.provider.DisruptorProvider;
import com.ahcloud.gateway.register.common.type.DataTypeParent;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/5/29 10:21
 **/
public class RegisterClientServerDisruptorPublisher implements GatewayClientServerRegisterPublisher {
    private static final RegisterClientServerDisruptorPublisher INSTANCE = new RegisterClientServerDisruptorPublisher();

    private DisruptorProviderManager<Collection<DataTypeParent>> providerManager;

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static RegisterClientServerDisruptorPublisher getInstance() {
        return INSTANCE;
    }

    /**
     * start.
     *
     * @param shenyuClientRegisterService the shenyu client register service
     */
    public void start(final Map<String, GatewayClientRegisterService> shenyuClientRegisterService) {
        RegisterServerConsumerExecutor.RegisterServerExecutorFactory factory = new RegisterServerConsumerExecutor.RegisterServerExecutorFactory();
        factory.addSubscribers(new RouteRegisterExecutorSubscriber(shenyuClientRegisterService));
        factory.addSubscribers(new MetadataExecutorSubscriber(shenyuClientRegisterService));
        providerManager = new DisruptorProviderManager<>(factory);
        providerManager.startup();
    }

    @Override
    public void publish(Collection<? extends DataTypeParent> dataList) {
        DisruptorProvider<Collection<DataTypeParent>> provider = providerManager.getProvider();
        provider.onData(dataList.stream().map(DataTypeParent.class::cast).collect(Collectors.toList()));
    }

    @Override
    public void publish(DataTypeParent data) {
        DisruptorProvider<Collection<DataTypeParent>> provider = providerManager.getProvider();
        provider.onData(Collections.singleton(data));
    }

    @Override
    public void close() {
        providerManager.getProvider().shutdown();
    }
}
