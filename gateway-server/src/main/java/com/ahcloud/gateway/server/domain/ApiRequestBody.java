package com.ahcloud.gateway.server.domain;

import lombok.*;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2022/12/7 15:48
 **/
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ApiRequestBody {

    /**
     * 接口标识
     */
    private String api;

    /**
     * 请求参数
     */
    private Object params;

    /**
     * 请求版本
     */
    private String version;

    /**
     * 时间戳
     */
    private Long timestamp;

    /**
     * 生成随机数
     */
    private String nonce;
}
