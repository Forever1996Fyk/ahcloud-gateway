package com.ahcloud.common.utils;

import com.google.common.collect.ImmutableMap;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @program: ahcloud-common
 * @description:
 * @author: YuKai Fan
 * @create: 2022/11/24 17:27
 **/
public class CollectionUtils {

    /**
     * 集合是否为空
     * @param collection 集合
     * @return 是否为空
     */
    public static boolean isEmpty(@Nullable Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * 集合不为空
     * @param collection 集合
     * @return 是否为空
     */
    public static boolean isNotEmpty(@Nullable Collection<?> collection) {
        return !isEmpty(collection);
    }

    /**
     * Map集合不为空
     * @param map 集合
     * @return 是否为空
     */
    public static boolean isNotEmpty(@Nullable Map<?, ?> map) {
        return !isEmpty(map);
    }

    /**
     * Map集合为空
     * @param map 集合
     * @return 是否为空
     */
    public static boolean isEmpty(@Nullable Map<?, ?> map) {
        return map == null || map.isEmpty();
    }


    /**
     * 此方法用于判断数组对象
     *
     * 即 一个数组中是否包含一个元素
     *
     * @param source 源元素
     * @param targets 目标数组
     * @return boolean
     */
    public static boolean containsAny(Object source, Object... targets) {
        return Arrays.asList(targets).contains(source);
    }

    /**
     * 根据某一条件过滤集合，并生成一个心机和
     * @param from  原集合
     * @param predicate 过滤断言条件
     * @return 新集合
     */
    public static <T> List<T> filterList(Collection<T> from, Predicate<T> predicate) {
        if (isEmpty(from)) {
            return new ArrayList<>();
        }
        return from.stream().filter(predicate).collect(Collectors.toList());
    }

    /**
     * 集合去重
     *
     * @param from 集合
     * @param keyMapper 去重条件
     * @return
     */
    public static <T, R> List<T> distinct(Collection<T> from, Function<T, R> keyMapper) {
        if (isEmpty(from)) {
            return new ArrayList<>();
        }
        return distinct(from, keyMapper, (t1, t2) -> t1);
    }

    /**
     * 集合去重
     *
     * @param from 集合
     * @param keyMapper 去重条件
     * @param cover  运算逻辑 (如果重复了，选择前一个还是后一个 (k1 ,k2) -> k1, (k1, k2) -> k2)
     * @return 去重后集合
     */
    public static <T, R> List<T> distinct(Collection<T> from, Function<T, R> keyMapper, BinaryOperator<T> cover) {
        if (isEmpty(from)) {
            return new ArrayList<>();
        }
        return new ArrayList<>(convertMap(from, keyMapper, Function.identity(), cover).values());
    }

    /**
     * 根据条件转化成新集合
     *
     * @param from 源集合
     * @param func 函数式接口
     * @return 新集合
     */
    public static <T, U> List<U> convertList(Collection<T> from, Function<T, U> func) {
        if (isEmpty(from)) {
            return new ArrayList<>();
        }
        return from.stream().map(func).filter(Objects::nonNull).collect(Collectors.toList());
    }

    /**
     * 根据条件转化成新集合
     *
     * @param from 源集合
     * @param func 函数式接口
     * @param filter 断言条件
     * @return 新集合
     */
    public static <T, U> List<U> convertList(Collection<T> from, Function<T, U> func, Predicate<T> filter) {
        if (isEmpty(from)) {
            return new ArrayList<>();
        }
        return from.stream().filter(filter).map(func).filter(Objects::nonNull).collect(Collectors.toList());
    }

    /**
     * 根据条件转化成新Set
     *
     * @param from 源集合
     * @param func 函数式接口
     * @return 新集合
     */
    public static <T, U> Set<U> convertSet(Collection<T> from, Function<T, U> func) {
        if (isEmpty(from)) {
            return new HashSet<>();
        }
        return from.stream().map(func).filter(Objects::nonNull).collect(Collectors.toSet());
    }

    /**
     * 根据条件转化成新Set
     *
     * @param from 源集合
     * @param func 函数式接口
     * @param filter 断言条件
     * @return 新集合
     */
    public static <T, U> Set<U> convertSet(Collection<T> from, Function<T, U> func, Predicate<T> filter) {
        if (isEmpty(from)) {
            return new HashSet<>();
        }
        return from.stream().filter(filter).map(func).filter(Objects::nonNull).collect(Collectors.toSet());
    }

    /**
     * 集合转为Map
     * @param from 源集合
     * @param keyFunc 函数式接口
     * @return Map
     */
    public static <T, K> Map<K, T> convertMap(Collection<T> from, Function<T, K> keyFunc) {
        if (isEmpty(from)) {
            return new HashMap<>();
        }
        return convertMap(from, keyFunc, Function.identity());
    }

    /**
     * 集合转为Map
     * @param from 源集合
     * @param keyFunc 函数式接口
     * @return Map
     */
    public static <T, K> Map<K, T> convertMap(Collection<T> from, Function<T, K> keyFunc, Supplier<? extends Map<K, T>> supplier) {
        if (isEmpty(from)) {
            return supplier.get();
        }
        return convertMap(from, keyFunc, Function.identity(), supplier);
    }

    /**
     * 集合转为Map
     * @param from 源集合
     * @param keyFunc 函数式接口
     * @return Map
     */
    public static <T, K, V> Map<K, V> convertMap(Collection<T> from, Function<T, K> keyFunc, Function<T, V> valueFunc) {
        if (isEmpty(from)) {
            return new HashMap<>();
        }
        return convertMap(from, keyFunc, valueFunc, (v1, v2) -> v1);
    }

    /**
     * 集合转为Map
     * @param from 源集合
     * @param keyFunc 函数式接口
     * @return Map
     */
    public static <T, K, V> Map<K, V> convertMap(Collection<T> from, Function<T, K> keyFunc, Function<T, V> valueFunc, BinaryOperator<V> mergeFunction) {
        if (isEmpty(from)) {
            return new HashMap<>();
        }
        return convertMap(from, keyFunc, valueFunc, mergeFunction, HashMap::new);
    }

    /**
     * 集合转为Map
     * @param from 源集合
     * @param keyFunc 函数式接口
     * @return Map
     */
    public static <T, K, V> Map<K, V> convertMap(Collection<T> from, Function<T, K> keyFunc, Function<T, V> valueFunc, Supplier<? extends Map<K, V>> supplier) {
        if (isEmpty(from)) {
            return supplier.get();
        }
        return convertMap(from, keyFunc, valueFunc, (v1, v2) -> v1, supplier);
    }

    /**
     * 集合转为Map
     * @param from 源集合
     * @param keyFunc 函数式接口
     * @return Map
     */
    public static <T, K, V> Map<K, V> convertMap(Collection<T> from, Function<T, K> keyFunc, Function<T, V> valueFunc, BinaryOperator<V> mergeFunction, Supplier<? extends Map<K, V>> supplier) {
        if (isEmpty(from)) {
            return new HashMap<>();
        }
        return from.stream().collect(Collectors.toMap(keyFunc, valueFunc, mergeFunction, supplier));
    }

    /**
     * 集合转为Map
     * @param from 源集合
     * @param keyFunc 函数式接口
     * @return Map
     */
    public static <T, K> Map<K, List<T>> convertMultiMap(Collection<T> from, Function<T, K> keyFunc) {
        if (isEmpty(from)) {
            return new HashMap<>();
        }
        return from.stream().collect(Collectors.groupingBy(keyFunc, Collectors.mapping(t -> t, Collectors.toList())));
    }

    /**
     * 集合转为Map
     * @param from 源集合
     * @param keyFunc 函数式接口
     * @return Map
     */
    public static <T, K, V> Map<K, List<V>> convertMultiMap(Collection<T> from, Function<T, K> keyFunc, Function<T, V> valueFunc) {
        if (isEmpty(from)) {
            return new HashMap<>();
        }
        return from.stream()
                .collect(Collectors.groupingBy(keyFunc, Collectors.mapping(valueFunc, Collectors.toList())));
    }

    /**
     * 集合转为Map
     * @param from 源集合
     * @param keyFunc 函数式接口
     * @return Map
     */
    public static <T, K, V> Map<K, Set<V>> convertMultiMap2(Collection<T> from, Function<T, K> keyFunc, Function<T, V> valueFunc) {
        if (isEmpty(from)) {
            return new HashMap<>();
        }
        return from.stream().collect(Collectors.groupingBy(keyFunc, Collectors.mapping(valueFunc, Collectors.toSet())));
    }

    /**
     * 集合转为 不可变 Map
     * @param from 源集合
     * @param keyFunc 函数式接口
     * @return Map
     */
    public static <T, K> Map<K, T> convertImmutableMap(Collection<T> from, Function<T, K> keyFunc) {
        if (isEmpty(from)) {
            return Collections.emptyMap();
        }
        ImmutableMap.Builder<K, T> builder = ImmutableMap.builder();
        from.forEach(item -> builder.put(keyFunc.apply(item), item));
        return builder.build();
    }

    /**
     * 其中一个集合在另一个集合中是否至少包含一个元素，即是两个集合是否至少有一个共同的元素
     * @param coll1 集合1
     * @param coll2 集合2
     * @return boolean
     */
    public static boolean containsAny(Collection<?> coll1, Collection<?> coll2) {
        if (isEmpty(coll1) || isEmpty(coll2)) {
            return false;
        }
        if (coll1.size() < coll2.size()) {
            for (Object object : coll1) {
                if (coll2.contains(object)) {
                    return true;
                }
            }
        } else {
            for (Object object : coll2) {
                if (coll1.contains(object)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 集合1中是否包含集合2中所有的元素，即集合2是否为集合1的子集
     *
     * @param coll1 集合1
     * @param coll2 集合2
     * @return boolean
     */
    public static boolean containsAll(Collection<?> coll1, Collection<?> coll2) {
        if (isEmpty(coll1)) {
            return isEmpty(coll2);
        }

        if (isEmpty(coll2)) {
            return true;
        }

        if (coll1.size() < coll2.size()) {
            return false;
        }

        for (Object object : coll2) {
            if (!coll1.contains(object)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取集合的第一个元素
     * @param from
     * @return
     * @param <T>
     */
    public static <T> T getFirst(List<T> from) {
        return !isEmpty(from) ? from.get(0) : null;
    }

    /**
     * 获取集合中满足条件的第一个元素
     * @param from
     * @param predicate
     * @return
     * @param <T>
     */
    public static <T> T findFirst(List<T> from, Predicate<T> predicate) {
        if (isEmpty(from)) {
            return null;
        }
        return from.stream().filter(predicate).findFirst().orElse(null);
    }

    /**
     * 获取集合中最大的值
     * @param from
     * @param valueFunc
     * @return
     */
    public static <T, V extends Comparable<? super V>> V getMaxValue(List<T> from, Function<T, V> valueFunc) {
        if (isEmpty(from)) {
            return null;
        }
        assert from.size() > 0; // 断言，避免告警
        T t = from.stream().max(Comparator.comparing(valueFunc)).get();
        return valueFunc.apply(t);
    }

    /**
     * 如果集合不为null, 则向集合中添加元素
     * @param coll
     * @param item
     * @param <T>
     */
    public static <T> void addIfNotNull(Collection<T> coll, T item) {
        if (item == null) {
            return;
        }
        coll.add(item);
    }
}
