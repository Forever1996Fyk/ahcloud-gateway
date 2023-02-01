package com.ahcloud.gateway.server.domain.admin.bo;

import lombok.*;

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
    private Integer expireAt;

    /**
     * 创建时间
     */
    private Integer issuedAt;

    /**
     * token类型
     */
    private String tokenType;
}
