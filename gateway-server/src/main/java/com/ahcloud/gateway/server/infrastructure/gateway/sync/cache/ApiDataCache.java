package com.ahcloud.gateway.server.infrastructure.gateway.sync.cache;

import com.ahcloud.gateway.core.domain.api.bo.ApiDefinitionPatternDTO;
import com.ahcloud.gateway.core.domain.api.dto.ApiDefinitionDTO;
import com.google.common.collect.Maps;
import org.bouncycastle.asn1.cms.MetaData;
import org.springframework.http.server.PathContainer;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/5/29 23:24
 **/
public class ApiDataCache {

    private static final String DEFAULT_CACHE_KEY = "";
    private static final ApiDefinitionDTO NULL = new ApiDefinitionDTO();
    private static final ApiDataCache INSTANCE = new ApiDataCache();

    private final static PathPatternParser DEFAULT_PATH_PARSER = new PathPatternParser();

    /**
     * apiCode -> apiDefinition
     */
    private static final ConcurrentMap<String, ApiDefinitionDTO> API_DATA_MAP = Maps.newConcurrentMap();

    /**
     * path -> apiDefinition
     * <p>
     * 这里的key存入的是具体的请求path,
     * 例如: apiPath为spring/**, apiDefinition, 此时的请求为: spring/A
     * 那么存入缓存的结果为: key: spring/A, value: apiDefinition
     */
    private static final Map<String, ApiDefinitionDTO> CACHE = Maps.newHashMap();

    /**
     * pathPattern -> path.
     * 这是缓存path格式与path的关系
     * 例如： apiPath为spring/**, 此时的请求为 spring/A, spring/B
     * 那么就会存入缓存  key: spring/**, value: [spring/A, spring/B]
     */
    private static final ConcurrentMap<String, Set<String>> MAPPING = Maps.newConcurrentMap();

    private ApiDataCache() {
    }

    public static ApiDataCache getInstance() {
        return INSTANCE;
    }

    /**
     * 缓存api
     *
     * @param apiDefinitionDTO
     */
    public void cache(final ApiDefinitionDTO apiDefinitionDTO) {
        // 清楚旧路径
        if (API_DATA_MAP.containsKey(apiDefinitionDTO.getApiCode())) {
            // 这里必须要执行一次清楚，因为不知道是create还是update
            clean(API_DATA_MAP.get(apiDefinitionDTO.getApiCode()).getPath());
        }
        API_DATA_MAP.put(apiDefinitionDTO.getApiCode(), apiDefinitionDTO);
        final String path = apiDefinitionDTO.getPath();
        clean(path);
        if (!path.contains("*")) {
            // 只有当path包含"*"时，才需要路径匹配
            initCache(path, apiDefinitionDTO, path);
        }
    }


    /**
     * 移除缓存
     *
     * @param apiDefinitionDTO
     */
    public void remove(final ApiDefinitionDTO apiDefinitionDTO) {
        API_DATA_MAP.remove(apiDefinitionDTO.getApiCode());
        clean(apiDefinitionDTO.getPath());
        MAPPING.remove(apiDefinitionDTO.getPath());
    }

    /**
     * 移除路径缓存
     * @param keys
     */
    public void removePathCache(final Collection<String> keys) {
        keys.forEach(key -> {
            clean(key);
            MAPPING.remove(key);
        });
    }

    private void clean(final String key) {
        Optional.ofNullable(MAPPING.get(key))
                .ifPresent(paths -> {
                    for (String path : paths) {
                        CACHE.remove(path);
                    }
                });
    }

    /**
     * 获取数据
     *
     * @param pathContainer
     * @return
     */
    public ApiDefinitionDTO obtain(final PathContainer pathContainer) {
        String path = pathContainer.value();
        ApiDefinitionDTO apiDefinitionDTO = Optional.ofNullable(CACHE.get(path))
                .orElseGet(() -> {
                    final ApiDefinitionDTO value = API_DATA_MAP.values().stream()
                            .filter(data -> DEFAULT_PATH_PARSER.parse(data.getPath()).matches(pathContainer))
                            .findFirst()
                            .orElse(null);
                    final String apiPath = Optional.ofNullable(value)
                            .map(ApiDefinitionDTO::getPath)
                            .orElse(DEFAULT_CACHE_KEY);
                    // 初始化缓存
                    initCache(path, value, apiPath);
                    return value;
                });
        return NULL.equals(apiDefinitionDTO) ? null : apiDefinitionDTO;
    }

    /**
     * cacheMap
     * @param path request path
     * @param value ApiDefinitionDTO
     * @param apiPath apiPath
     */
    private void initCache(final String path, final ApiDefinitionDTO value, final String apiPath) {
        // 这里有存在OOM的风险，可能需要LRU
        CACHE.put(path, Optional.ofNullable(value).orElse(NULL));
        // spring/** -> Collections 'spring/A', 'spring/B'
        Set<String> paths = MAPPING.get(apiPath);
        if (Objects.isNull(paths)) {
            MAPPING.putIfAbsent(apiPath, new ConcurrentSkipListSet<>());
            paths = MAPPING.get(apiPath);
        }
        paths.add(path);
    }
}
