package com.ahcloud.gateway.core.domain.api.vo;

import lombok.*;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/5/29 18:35
 **/
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AppNameSelectVO {

    /**
     * 应用名
     */
    private String appName;

    /**
     * 环境变量
     */
    private String env;
}
