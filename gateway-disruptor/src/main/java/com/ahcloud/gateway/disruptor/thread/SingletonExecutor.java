package com.ahcloud.gateway.disruptor.thread;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @program: ahcloud-gateway
 * @description: 核心线程数只有1的线程池
 * @author: YuKai Fan
 * @create: 2023/5/26 09:54
 **/
public class SingletonExecutor extends ThreadPoolExecutor {

    public SingletonExecutor(final ThreadFactory factory) {
        super(1, 1, 0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(), factory);
    }
}
