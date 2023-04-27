package com.ahcloud.gateway.core.domain.api.bo;

import com.ahcloud.gateway.core.domain.api.dto.ApiRefreshDTO;
import lombok.Data;
import org.springframework.web.util.pattern.PathPattern;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/2/21 09:00
 **/
@Data
public class ApiRefreshPatternDTO {

    /**
     * 路径匹配
     */
    private final PathPattern pathPattern;

    /**
     * api刷新实体
     */
    private final ApiRefreshDTO apiRefreshDTO;

    public ApiRefreshPatternDTO(PathPattern pathPattern, ApiRefreshDTO apiRefreshDTO) {
        this.pathPattern = pathPattern;
        this.apiRefreshDTO = apiRefreshDTO;
    }
}
