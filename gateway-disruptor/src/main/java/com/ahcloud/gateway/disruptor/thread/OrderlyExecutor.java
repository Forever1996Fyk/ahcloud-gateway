package com.ahcloud.gateway.disruptor.thread;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;
import java.util.SortedMap;
import java.util.concurrent.*;
import java.util.stream.IntStream;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/5/26 09:53
 **/
public class OrderlyExecutor extends ThreadPoolExecutor {
    private final ConcurrentSkipListMap<Long, SingletonExecutor> virtualExecutors = new ConcurrentSkipListMap<>();

    private final ThreadSelector threadSelector = new ThreadSelector();

    /**
     * Instantiates a new Orderly executor.
     *
     * @param corePoolSize    the core pool size
     * @param maximumPoolSize the maximum pool size
     * @param keepAliveTime   the keep alive time
     * @param unit            the unit
     * @param workQueue       the work queue
     * @param threadFactory   the thread factory
     * @param handler         the handler
     * @param isOrderly the orderly Whether to execute sequentially.
     */
    public OrderlyExecutor(
            final boolean isOrderly,
            final int corePoolSize,
            final int maximumPoolSize,
            final long keepAliveTime,
            final TimeUnit unit,
            final BlockingQueue<Runnable> workQueue,
            final ThreadFactory threadFactory,
            final RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
        orderlyThreadPool(isOrderly, corePoolSize, threadFactory);
    }

    private void orderlyThreadPool(final boolean isOrderly, final int corePoolSize, final ThreadFactory threadFactory) {
        if (isOrderly) {
            IntStream.range(0, corePoolSize).forEach(index -> {
                SingletonExecutor singletonExecutor = new SingletonExecutor(threadFactory);
                String hash = singletonExecutor.hashCode() + ":" + index;
                byte[] bytes = threadSelector.sha(hash);
                for (int i = 0; i < 4; i++) {
                    this.virtualExecutors.put(threadSelector.hash(bytes, i), singletonExecutor);
                }
            });
        }
    }

    /**
     * 根据hash值获取 线程池执行
     * @param hash
     * @return
     */
    public SingletonExecutor select(final String hash) {
        long select = threadSelector.select(hash);
        if (!virtualExecutors.containsKey(select)) {
            SortedMap<Long, SingletonExecutor> tailMap = virtualExecutors.tailMap(select);
            if (tailMap.isEmpty()) {
                select = virtualExecutors.firstKey();
            } else {
                select = tailMap.firstKey();
            }
        }
        return virtualExecutors.get(select);
    }

    /**
     * 线程选择器
     */
    private static final class ThreadSelector {

        /**
         * Select long.
         *
         * @param hash the hash
         * @return the long
         */
        public long select(final String hash) {
            byte[] digest = sha(hash);
            return hash(digest, 0);
        }

        /**
         * ketame  hash.
         */
        private long hash(final byte[] digest, final int number) {
            return (((long) (digest[3 + number * 4] & 0xFF) << 24)
                    | ((long) (digest[2 + number * 4] & 0xFF) << 16)
                    | ((long) (digest[1 + number * 4] & 0xFF) << 8)
                    | (digest[number * 4] & 0xFF))
                    & 0xFFFFFFFFL;
        }

        private byte[] sha(final String hash) {
            byte[] bytes = hash.getBytes(StandardCharsets.UTF_8);
            return Hashing
                    .sha256()
                    .newHasher()
                    .putBytes(bytes)
                    .hash().asBytes();
        }
    }
}
