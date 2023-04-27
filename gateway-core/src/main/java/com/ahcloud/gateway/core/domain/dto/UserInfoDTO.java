package com.ahcloud.gateway.core.domain.dto;

import lombok.*;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/2/21 10:07
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDTO {

    /**
     * 用户id
     */
    private String userId;

    /**
     * 租户id
     */
    private Long tenantId;

    /**
     * 请求token
     */
    private String token;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 昵称
     */
    private String nickName;
}
