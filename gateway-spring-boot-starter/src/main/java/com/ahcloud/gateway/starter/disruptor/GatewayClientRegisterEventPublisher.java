package com.ahcloud.gateway.starter.disruptor;

import com.ahcloud.gateway.disruptor.DisruptorProviderManager;
import com.ahcloud.gateway.disruptor.provider.DisruptorProvider;
import com.ahcloud.gateway.register.common.type.DataTypeParent;
import com.ahcloud.gateway.starter.disruptor.executor.RegisterClientConsumerExecutor;
import com.ahcloud.gateway.starter.disruptor.subscriber.GatewayClientMetadataExecutorSubscriber;
import com.ahcloud.gateway.starter.disruptor.subscriber.GatewayClientRouteExecutorSubscriber;
import com.ahcloud.gateway.starter.repository.GatewayClientRegisterRepository;

import java.util.Collection;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/5/26 09:45
 **/
public class GatewayClientRegisterEventPublisher {

    private static final GatewayClientRegisterEventPublisher INSTANCE = new GatewayClientRegisterEventPublisher();

    private DisruptorProviderManager<DataTypeParent> providerManager;

    public static GatewayClientRegisterEventPublisher getInstance() {
        return INSTANCE;
    }

    private GatewayClientRegisterEventPublisher() {}

    /**
     * 启动  Disruptor队列 数据订阅
     * @param repository
     */
    public void start(final GatewayClientRegisterRepository repository) {
        RegisterClientConsumerExecutor.RegisterClientExecutorFactory factory = new RegisterClientConsumerExecutor.RegisterClientExecutorFactory();
        factory.addSubscribers(new GatewayClientRouteExecutorSubscriber(repository));
        factory.addSubscribers(new GatewayClientMetadataExecutorSubscriber(repository));
        providerManager = new DisruptorProviderManager<>(factory);
        providerManager.startup();
    }

    public void publishEvent(final DataTypeParent dataTypeParent) {
        DisruptorProvider<DataTypeParent> provider = providerManager.getProvider();
        provider.onData(dataTypeParent);
    }
}
