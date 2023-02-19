package com.ahcloud.gateway.server.infrastructure.security.token.authentication.bo;

import lombok.*;

import java.io.Serializable;
import java.time.Instant;

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
public class AccessTokenBO implements Serializable {

    private static final long serialVersionUID = -7605728814367901450L;
    /**
     * token
     */
    private String token;

    /**
     * 过期时间
     */
    private Instant expireAt;

    /**
     * 创建时间
     */
    private Instant issuedAt;

    /**
     * 还剩多长时间(秒)
     */
    private Integer expiresIn;
}
