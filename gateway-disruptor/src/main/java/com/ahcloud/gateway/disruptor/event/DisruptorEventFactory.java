package com.ahcloud.gateway.disruptor.event;

import com.lmax.disruptor.EventFactory;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/5/26 11:18
 **/
public class DisruptorEventFactory<T> implements EventFactory<DataEvent<T>> {
    @Override
    public DataEvent<T> newInstance() {
        return new DataEvent<>();
    }
}
