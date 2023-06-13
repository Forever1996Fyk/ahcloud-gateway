package com.ahcloud.gateway.client.common.concurrent;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/6/13 10:24
 **/
public class MemoryLimitCalculator {

    private static volatile long maxAvailable;

    private static final AtomicBoolean REFRESH_STARTED = new AtomicBoolean(false);

    private static void checkAndScheduleRefresh() {
        if (!REFRESH_STARTED.get()) {
            // see https://github.com/apache/dubbo/pull/10178
            refresh();
            if (REFRESH_STARTED.compareAndSet(false, true)) {
                ScheduledExecutorService scheduledExecutorService =
                        new ScheduledThreadPoolExecutor(1, GatewayThreadFactory.create("Shenyu-Memory-Calculator-", false));
                // check every 50 ms to improve performance
                scheduledExecutorService.scheduleWithFixedDelay(MemoryLimitCalculator::refresh, 50, 50, TimeUnit.MILLISECONDS);
                Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                    REFRESH_STARTED.set(false);
                    scheduledExecutorService.shutdown();
                }
                ));
            }
        }
    }

    private static void refresh() {
        maxAvailable = Runtime.getRuntime().freeMemory();
    }

    /**
     * Get the maximum available memory of the current JVM.
     *
     * @return maximum available memory
     */
    public static long maxAvailable() {
        checkAndScheduleRefresh();
        return maxAvailable;
    }

    /**
     * Take the current JVM's maximum available memory
     * as a percentage of the result as the limit.
     *
     * @param percentage percentage
     * @return available memory
     */
    public static long calculate(final float percentage) {
        if (percentage <= 0 || percentage > 1) {
            throw new IllegalArgumentException();
        }
        checkAndScheduleRefresh();
        return (long) (maxAvailable() * percentage);
    }

    /**
     * By default, it takes 80% of the maximum available memory of the current JVM.
     *
     * @return available memory
     */
    public static long defaultLimit() {
        checkAndScheduleRefresh();
        return (long) (maxAvailable() * 0.8);
    }
}
