package com.ahcloud.gateway.core.infrastructure.util;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.time.Duration;
import java.util.concurrent.Executors;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/4/27 16:07
 **/
public class CacheUtils {

    /**
     * 构建异常加载缓存
     * @param duration
     * @param loader
     * @return
     * @param <K>
     * @param <V>
     */
    public static <K, V> LoadingCache<K, V> buildAsyncReloadingCache(Duration duration, CacheLoader<K, V> loader) {
        return CacheBuilder.newBuilder()
                // 只阻塞当前数据加载线程，其他线程返回旧值
                .refreshAfterWrite(duration)
                // 通过 asyncReloading 实现全异步加载，包括 refreshAfterWrite 被阻塞的加载线程
                // TODO：可能要思考下，未来要不要做成可配置
                .build(CacheLoader.asyncReloading(loader, Executors.newCachedThreadPool()));
    }
}
