package com.ahcloud.gateway.disruptor.event;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/5/26 09:57
 **/
public class OrderlyDataEvent<T> extends DataEvent<T> {

    private String hash;

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}
