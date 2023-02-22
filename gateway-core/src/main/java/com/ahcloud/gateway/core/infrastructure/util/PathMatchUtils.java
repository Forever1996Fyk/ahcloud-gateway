package com.ahcloud.gateway.core.infrastructure.util;

import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import java.util.Set;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/1/16 14:57
 **/
public class PathMatchUtils {
    private final static PathMatcher PATH_MATCHER = new AntPathMatcher();

    /**
     * 路径匹配
     * @param path
     * @param pathSet
     * @return
     */
    public static boolean pathAnyMatch(String path, Set<String> pathSet) {
        return pathSet.stream()
                .anyMatch(item -> PATH_MATCHER.match(item, path));
    }
}
