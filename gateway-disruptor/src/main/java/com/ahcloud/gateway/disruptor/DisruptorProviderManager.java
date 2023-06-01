package com.ahcloud.gateway.disruptor;

import com.ahcloud.gateway.disruptor.consumer.QueueConsumer;
import com.ahcloud.gateway.disruptor.consumer.QueueConsumerFactory;
import com.ahcloud.gateway.disruptor.event.DataEvent;
import com.ahcloud.gateway.disruptor.event.DisruptorEventFactory;
import com.ahcloud.gateway.disruptor.event.OrderlyDisruptorEventFactory;
import com.ahcloud.gateway.disruptor.provider.DisruptorProvider;
import com.ahcloud.gateway.disruptor.thread.DisruptorThreadFactory;
import com.ahcloud.gateway.disruptor.thread.OrderlyExecutor;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.IgnoreExceptionHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/5/26 09:32
 **/
public class DisruptorProviderManager<T> {

    public static final Integer DEFAULT_SIZE = 4096 << 1 << 1;

    private static final Integer DEFAULT_CONSUMER_SIZE = Runtime.getRuntime().availableProcessors() << 1;

    private final Integer size;

    private final Integer consumerSize;

    private final QueueConsumerFactory<T> consumerFactory;

    private DisruptorProvider<T> provider;

    /**
     * Instantiates a new Disruptor provider manage.
     *
     * @param consumerFactory the consumer factory
     */
    public DisruptorProviderManager(final QueueConsumerFactory<T> consumerFactory) {
        this(consumerFactory, DEFAULT_CONSUMER_SIZE, DEFAULT_SIZE);
    }

    /**
     * Instantiates a new Disruptor provider manage.
     *
     * @param consumerFactory the consumer factory
     * @param consumerSize    the consumer size
     * @param ringBufferSize  the ringBuffer size
     */
    public DisruptorProviderManager(final QueueConsumerFactory<T> consumerFactory,
                                   final int consumerSize,
                                   final int ringBufferSize) {
        this.consumerFactory = consumerFactory;
        this.size = ringBufferSize;
        this.consumerSize = consumerSize;
    }

    /**
     * start disruptor.
     */
    public void startup() {
        this.startup(false);
    }

    /**
     * start disruptor..
     *
     * @param isOrderly the orderly Whether to execute sequentially.
     */
    public void startup(final boolean isOrderly) {
        OrderlyExecutor executor = new OrderlyExecutor(isOrderly, consumerSize, consumerSize, 0, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(),
                DisruptorThreadFactory.create("shenyu_disruptor_consumer_", false), new ThreadPoolExecutor.AbortPolicy());
        int newConsumerSize = this.consumerSize;
        EventFactory<DataEvent<T>> eventFactory;
        if (isOrderly) {
            newConsumerSize = 1;
            eventFactory = new OrderlyDisruptorEventFactory<>();
        } else {
            eventFactory = new DisruptorEventFactory<>();
        }
        Disruptor<DataEvent<T>> disruptor = new Disruptor<>(eventFactory,
                size,
                DisruptorThreadFactory.create("gateway_disruptor_provider_" + consumerFactory.fixName(), false),
                ProducerType.MULTI,
                new BlockingWaitStrategy());
        @SuppressWarnings("all")
        QueueConsumer<T>[] consumers = new QueueConsumer[newConsumerSize];
        for (int i = 0; i < newConsumerSize; i++) {
            consumers[i] = new QueueConsumer<>(executor, consumerFactory);
        }
        disruptor.handleEventsWithWorkerPool(consumers);
        disruptor.setDefaultExceptionHandler(new IgnoreExceptionHandler());
        disruptor.start();
        RingBuffer<DataEvent<T>> ringBuffer = disruptor.getRingBuffer();
        provider = new DisruptorProvider<>(ringBuffer, disruptor, isOrderly);
    }

    /**
     * 获取 DisruptorProvider
     *
     * @return the provider
     */
    public DisruptorProvider<T> getProvider() {
        return provider;
    }

}
