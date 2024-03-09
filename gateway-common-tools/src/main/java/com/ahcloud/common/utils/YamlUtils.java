package com.ahcloud.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @program: ahcloud-common
 * @description:
 * @author: YuKai Fan
 * @create: 2023/2/23 21:09
 **/
@Slf4j
public class YamlUtils {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper(new YAMLFactory());

    /**
     * yaml转为实体
     * @param yamlString
     * @param clazz
     * @return
     * @param <T>
     */
    public static <T> T yamlToBean(String yamlString, Class<T> clazz) {
        if (StringUtils.isBlank(yamlString)) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(yamlString, clazz);
        } catch (JsonProcessingException e) {
            log.error("jackson yaml to class error, params:{}, exception:{}", yamlString, Throwables.getStackTraceAsString(e));
            return null;
        }
    }
}
