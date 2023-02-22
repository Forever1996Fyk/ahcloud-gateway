package com.ahcloud.gateway.core.domain.bo;

import com.ahcloud.gateway.client.enums.ApiStatusEnum;
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
public class ApiGatewayBO {
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
     * api状态
     */
    private ApiStatusEnum apiStatusEnum;
}
