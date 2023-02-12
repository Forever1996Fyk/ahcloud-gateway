package com.ahcloud.gateway.server.infrastructure.config.properties;

import com.google.common.collect.Sets;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/1/16 11:35
 **/
@Data
@Component
@RefreshScope
public class GatewayAuthProperties {

    /**
     * 忽略token的url集合
     */
    @Value(value = "${gateway.auth.ignoreTokenUrlSet:}")
    private String ignoreTokenUrlSet;

    /**
     * 忽略鉴权的url集合
     */
    @Value(value = "${gateway.auth.ignoreAuthUrlSet:}")
    private String ignoreAuthUrlSet;

    public Set<String> getIgnoreTokenUrlSet() {
        Set<String> set = Sets.newHashSet();
        if (ignoreTokenUrlSet == null || ignoreTokenUrlSet.length() == 0) {
            return set;
        }
        set.addAll(Arrays.asList(StringUtils.split(ignoreTokenUrlSet, ",")));
        return set;
    }

    public Set<String> getIgnoreAuthUrlSet() {
        Set<String> set = Sets.newHashSet();
        if (ignoreAuthUrlSet == null || ignoreAuthUrlSet.length() == 0) {
            return set;
        }
        set.addAll(Arrays.asList(StringUtils.split(ignoreAuthUrlSet, ",")));
        return set;
    }

    public String[] getIgnoreAuthUrlArray() {
        String[] array = new String[] {};
        Set<String> set = Sets.newHashSet();
        if (ignoreAuthUrlSet == null || ignoreAuthUrlSet.length() == 0) {
            return array;
        }
        return StringUtils.split(ignoreAuthUrlSet, ",");
    }
}
