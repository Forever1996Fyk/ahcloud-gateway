package com.ahcloud.gateway.core.domain.bo;

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
public class UserInfoBO {

    /**
     * 用户id
     */
    private String userId;

    /**
     * 租户id
     */
    private String tenantId;

    /**
     * 请求token
     */
    private String token;
}
