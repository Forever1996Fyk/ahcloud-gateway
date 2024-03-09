package com.ahcloud.common.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @program: ahcloud-common
 * @description:
 * @author: YuKai Fan
 * @create: 2022/11/24 17:19
 **/
@Slf4j
public class JsonUtils {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        // 属性为Null的不进行序列化，只对pojo起作用，对map和list不起作用
        OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        // 遇到未知属性是否抛出异常, 默认是抛出异常的
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * 转为json string
     *
     * @param t
     * @param <T>
     * @return
     */
    public static <T> String toJsonString(T t) {
        if (Objects.isNull(t)) {
            return null;
        }
        try {
            log.info("jackson object to jsonString, params:{}", t);
            return OBJECT_MAPPER.writeValueAsString(t);
        } catch (Exception e) {
            log.error("jackson object to jsonString error, params:{}, exception:{}", t, Throwables.getStackTraceAsString(e));
            return null;
        }
    }

    /**
     * json string转为bean
     *
     * @param json
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> T stringToBean(String json, Class<T> tClass) {
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        try {
            log.info("jackson jsonString to object, params:{}", json);
            return OBJECT_MAPPER.readValue(json, tClass);
        } catch (Exception e) {
            log.error("jackson jsonString to object  error, params:{}, exception:{}", json, Throwables.getStackTraceAsString(e));
            return null;
        }
    }

    /**
     * json string转为map
     *
     * @param json
     * @param <T>
     * @return
     */
    public static Map<String, Object> stringToMap(String json) {
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        try {
            log.info("jackson jsonString to map, params:{}", json);
            TypeReference<Map<String, Object>> typeRef = new TypeReference<Map<String, Object>>() {};
            return OBJECT_MAPPER.readValue(json, typeRef);
        } catch (Exception e) {
            log.error("jackson jsonString to object  error, params:{}, exception:{}", json, Throwables.getStackTraceAsString(e));
            return null;
        }
    }

    /**
     * json string转为map
     *
     * @param json
     * @param <T>
     * @return
     */
    public static <K, V>  Map<K, V> stringToMap2(String json) {
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        try {
            log.info("jackson jsonString to map, params:{}", json);
            TypeReference<Map<K, V>> typeRef = new TypeReference<Map<K, V>>() {};
            return OBJECT_MAPPER.readValue(json, typeRef);
        } catch (Exception e) {
            log.error("jackson jsonString to object  error, params:{}, exception:{}", json, Throwables.getStackTraceAsString(e));
            return null;
        }
    }

    /**
     * json字符串转成list
     *
     * @param jsonString
     * @param cls
     * @return
     */
    public static <T> List<T> jsonToList(String jsonString, Class<T> cls) {
        try {
            return OBJECT_MAPPER.readValue(jsonString, getCollectionType(List.class, cls));
        } catch (Exception e) {
            String className = cls.getSimpleName();
            log.error(" parse json [{}] to class [{}] error：{}", jsonString, className, e);
        }
        return null;
    }

    /**
     * json字符串转成list
     *
     * @param jsonString
     * @param cls
     * @return
     */
    public static <T> Set<T> jsonToSet(String jsonString, Class<T> cls) {
        try {
            return OBJECT_MAPPER.readValue(jsonString, getCollectionType(Set.class, cls));
        } catch (Exception e) {
            String className = cls.getSimpleName();
            log.error(" parse json [{}] to class [{}] error：{}", jsonString, className, e);
        }
        return null;
    }

    /**
     * 获取泛型的Collection Type
     *
     * @param collectionClass 泛型的Collection
     * @param elementClasses  实体bean
     * @return JavaType Java类型
     */
    private static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
        return OBJECT_MAPPER.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }

    /**
     * bytes convert bean
     *
     * @param bytes
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T byteToBean(byte[] bytes, Class<T> clazz) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        try {
            log.info("jackson bytes to object, bytes:{}, className:{}", bytes, clazz.getName());
            return OBJECT_MAPPER.readValue(bytes, clazz);
        } catch (Exception e) {
            log.error("jackson bytes to object  error, bytes:{}, className:{}, exception:{}", bytes, clazz.getName(), Throwables.getStackTraceAsString(e));
            return null;
        }
    }

    /**
     * bean 转为bytes
     * @param obj
     * @return
     * @param <T>
     */
    public static <T> byte[] beanToByte(T obj) {
        try {
            return OBJECT_MAPPER.writeValueAsBytes(obj);
        } catch (JsonProcessingException e) {
            log.error("jackson object to byte  error, obj:{}, exception:{}", obj,  Throwables.getStackTraceAsString(e));
            return null;
        }
    }

    /**
     * map转为bean
     * @param map
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T mapToBean(Map<String, Object> map, Class<T> clazz) {
        if (map == null || map.size() <= 0) {
            return null;
        }
        return OBJECT_MAPPER.convertValue(map, clazz);
    }

    /**
     * byte转为JsonNode
     *
     * @param bytes
     * @return
     */
    public static JsonNode byteToReadTree(byte[] bytes) {
        try {
            return OBJECT_MAPPER.readTree(bytes);
        } catch (Exception e) {
            log.error("jackson bytes read tree convert JsonNode  error, bytes:{}, exception:{}", bytes, Throwables.getStackTraceAsString(e));
            return null;
        }
    }

    /**
     * byte转为JsonNode
     *
     * @param content
     * @return
     */
    public static JsonNode byteToReadTree(String content) {
        try {
            return OBJECT_MAPPER.readTree(content);
        } catch (Exception e) {
            log.error("jackson bytes read tree convert JsonNode  error, bytes:{}, exception:{}", content, Throwables.getStackTraceAsString(e));
            return null;
        }
    }
}
