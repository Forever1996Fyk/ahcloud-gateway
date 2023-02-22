package com.ahcloud.gateway.core.domain.api.dto;

import com.ahcloud.gateway.client.enums.ApiStatusEnum;
import lombok.*;

import java.io.Serializable;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/2/20 23:17
 **/
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ApiRefreshDTO implements Serializable {
    private static final long serialVersionUID = -2032491110731433753L;

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
