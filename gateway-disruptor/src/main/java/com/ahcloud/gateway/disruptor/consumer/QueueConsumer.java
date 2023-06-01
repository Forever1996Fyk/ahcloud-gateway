package com.ahcloud.gateway.disruptor.consumer;

import com.ahcloud.gateway.disruptor.event.DataEvent;
import com.ahcloud.gateway.disruptor.event.OrderlyDataEvent;
import com.ahcloud.gateway.disruptor.thread.OrderlyExecutor;
import com.lmax.disruptor.WorkHandler;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/5/26 09:51
 **/
public class QueueConsumer<T> implements WorkHandler<DataEvent<T>> {

    private final OrderlyExecutor executor;

    private final QueueConsumerFactory<T> factory;

    public QueueConsumer(OrderlyExecutor executor, QueueConsumerFactory<T> factory) {
        this.executor = executor;
        this.factory = factory;
    }

    @Override
    public void onEvent(DataEvent<T> t) throws Exception {
        if (t != null) {
            ThreadPoolExecutor executor = orderly(t);
            QueueConsumerExecutor<T> queueConsumerExecutor = factory.create();
            queueConsumerExecutor.setData(t.getData());
            // help gc
            t.setData(null);
            executor.execute(queueConsumerExecutor);
        }
    }

    private ThreadPoolExecutor orderly(final DataEvent<T> t) {
        if (t instanceof OrderlyDataEvent && !isEmpty(((OrderlyDataEvent<T>) t).getHash())) {
            return executor.select(((OrderlyDataEvent<T>) t).getHash());
        } else {
            return executor;
        }
    }

    private boolean isEmpty(final String t) {
        return t == null || t.isEmpty();
    }
}
