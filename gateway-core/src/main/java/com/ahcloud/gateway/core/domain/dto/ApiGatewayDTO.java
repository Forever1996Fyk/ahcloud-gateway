package com.ahcloud.gateway.core.domain.dto;

import lombok.*;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/2/21 10:01
 **/
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ApiGatewayDTO {
    /**
     * api编码
     */
    private String apiCode;

    /**
     * api path
     */
    private String path;

    /**
     * 是否需要认证
     */
    private Boolean auth;

    /**
     * 开发环境状态
     */
    private Integer dev;

    /**
     * 联调环境状态
     */
    private Integer test;

    /**
     * 测试环境状态
     */
    private Integer sit;

    /**
     * 预发环境状态
     */
    private Integer pre;

    /**
     * 生产环境状态
     */
    private Integer prod;
}
