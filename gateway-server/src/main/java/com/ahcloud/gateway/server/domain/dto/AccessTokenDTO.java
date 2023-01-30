package com.ahcloud.gateway.server.domain.dto;

import lombok.*;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/1/20 18:39
 **/
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AccessTokenDTO {

    /**
     * token
     */
    private String token;

    /**
     * 过期时间
     */
    private Long expireTime;

    /**
     * 创建时间
     */
    private Long issuedTime;
}
