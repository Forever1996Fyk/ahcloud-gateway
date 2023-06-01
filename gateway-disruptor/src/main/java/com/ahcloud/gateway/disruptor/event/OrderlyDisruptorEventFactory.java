package com.ahcloud.gateway.disruptor.event;

import com.lmax.disruptor.EventFactory;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/5/26 11:17
 **/
public class OrderlyDisruptorEventFactory<T> implements EventFactory<DataEvent<T>> {
    @Override
    public DataEvent<T> newInstance() {
        return new OrderlyDataEvent<>();
    }
}
