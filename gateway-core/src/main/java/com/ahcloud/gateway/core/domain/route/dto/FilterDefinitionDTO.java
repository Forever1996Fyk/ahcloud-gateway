package com.ahcloud.gateway.core.domain.route.dto;

import lombok.*;

import java.util.Map;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/2/21 23:20
 **/
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FilterDefinitionDTO {

    /**
     * 过滤器名称
     */
    private String name;

    /**
     * 参数
     */
    private Map<String, String> args;
}
