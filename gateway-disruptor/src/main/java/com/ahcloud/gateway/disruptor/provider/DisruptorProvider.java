package com.ahcloud.gateway.disruptor.provider;

import com.ahcloud.gateway.disruptor.event.DataEvent;
import com.google.common.base.Throwables;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import lombok.extern.slf4j.Slf4j;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/5/26 11:18
 **/
@Slf4j
public class DisruptorProvider<T> {
    private final RingBuffer<DataEvent<T>> ringBuffer;

    private final Disruptor<DataEvent<T>> disruptor;

    private final boolean isOrderly;

    private final EventTranslatorOneArg<DataEvent<T>, T> translatorOneArg = (event, sequence, t) -> event.setData(t);

    /**
     * Instantiates a new Disruptor provider.
     *
     * @param ringBuffer the ring buffer
     * @param disruptor  the disruptor
     * @param isOrderly  the orderly Whether to execute sequentially.
     */
    public DisruptorProvider(final RingBuffer<DataEvent<T>> ringBuffer, final Disruptor<DataEvent<T>> disruptor, final boolean isOrderly) {
        this.ringBuffer = ringBuffer;
        this.disruptor = disruptor;
        this.isOrderly = isOrderly;
    }

    /**
     * 发送数据
     * @param data
     */
    public void onData(final T data) {
        if (isOrderly) {
            throw new IllegalArgumentException("The current provider is  of orderly type. Please use onOrderlyData() method.");
        }
        try {
            ringBuffer.publishEvent(translatorOneArg, data);
        } catch (Exception ex) {
            log.error("DisruptorProvider[onData] exception is {}", Throwables.getStackTraceAsString(ex));
        }
    }

    /**
     * Shutdown.
     */
    public void shutdown() {
        if (null != disruptor) {
            disruptor.shutdown();
        }
    }
}
