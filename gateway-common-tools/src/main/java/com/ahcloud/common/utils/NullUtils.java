package com.ahcloud.common.utils;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * @program: ahcloud-common
 * @description:
 * @author: YuKai Fan
 * @create: 2022/11/24 20:02
 **/
public class NullUtils {
    /**
     * 如果为空，返回""空串
     */
    public static String of(String str) {
        return Optional.ofNullable(str).orElse("");
    }

    public static Float of(Float f) {
        if (f == null) {
            return (float) 0;
        } else {
            return f;
        }
    }

    public static Long of(Long l) {
        return Optional.ofNullable(l).orElse(0L);
    }

    public static Long parseNull(Long l) {
        if (l != null && l == 0) {
            return null;
        }
        return l;
    }

    public static String parseNull(String l) {
        if (StringUtils.EMPTY.equals(l)) {
            return null;
        }
        return l;
    }

    public static Integer parseNull(Integer l) {
        if (l != null && l == 0) {
            return null;
        }
        return l;
    }

    public static Integer of(Integer i) {
        return Optional.ofNullable(i).orElse(0);
    }

    public static Integer of(Byte b) {

        if (b == null) {
            return 0;
        }

        return Integer.valueOf(b);
    }

    public static Double of(Double d) {
        if (d == null) {
            return 0.0;
        } else {
            return d;
        }
    }

    public static <T> Collection<T> of(Collection<T> collection) {
        if (CollectionUtils.isEmpty(collection)) {
            return Lists.newArrayList();
        } else {
            return collection;
        }
    }

    /**
     * 如果非空取字段的值
     */
    public static <T> String getStrField(T obj, Function<T, String> function) {
        return Objects.nonNull(obj) ? function.apply(obj) : StringUtils.EMPTY;
    }
}
