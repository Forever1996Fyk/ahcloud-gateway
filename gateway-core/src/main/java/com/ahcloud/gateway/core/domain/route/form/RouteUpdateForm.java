package com.ahcloud.gateway.core.domain.route.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/2/12 21:53
 **/
@Data
public class RouteUpdateForm {

    /**
     * serviceId
     */
    @NotEmpty(message = "服务id不能为空")
    private String serviceId;
}
