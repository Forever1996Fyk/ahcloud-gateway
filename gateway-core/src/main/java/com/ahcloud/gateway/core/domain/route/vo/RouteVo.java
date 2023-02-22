package com.ahcloud.gateway.core.domain.route.vo;

import lombok.*;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/2/12 21:56
 **/
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RouteVo {

    /**
     * 服务id
     */
    private String serviceId;
}
