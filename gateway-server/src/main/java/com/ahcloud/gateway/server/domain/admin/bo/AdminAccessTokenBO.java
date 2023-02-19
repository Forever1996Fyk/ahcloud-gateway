package com.ahcloud.gateway.server.domain.admin.bo;

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
public class AdminAccessTokenBO {

    /**
     * token
     */
    private String token;

    /**
     * 过期时间
     */
    private Date expireTime;

    /**
     * 创建时间
     */
    private Date issuedTime;

    /**
     * 还剩多长时间(秒)
     */
    private Integer expiresIn;

    /**
     * token类型
     */
    private String tokenType;
}
