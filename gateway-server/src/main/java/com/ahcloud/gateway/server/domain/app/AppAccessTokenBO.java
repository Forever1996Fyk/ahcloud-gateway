package com.ahcloud.gateway.server.domain.app;

import lombok.*;

import java.util.Date;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/1/31 16:31
 **/
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AppAccessTokenBO {

    /**
     * token
     */
    private String token;

    /**
     * 过期时间
     */
    private Date expireAt;

    /**
     * 创建时间
     */
    private Date issuedAt;

    /**
     * 还剩多长时间(秒)
     */
    private Integer expiresIn;

    /**
     * token类型
     */
    private String tokenType;
}
