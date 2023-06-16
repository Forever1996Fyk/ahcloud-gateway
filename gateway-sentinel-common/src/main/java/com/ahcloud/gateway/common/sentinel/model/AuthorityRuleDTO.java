package com.ahcloud.gateway.common.sentinel.model;

import com.ahcloud.gateway.common.sentinel.model.enums.AuthorityStrategyEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/6/15 10:00
 **/
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthorityRuleDTO {

    /**
     * 资源标识
     */
    private String resource;

    /**
     * 限制数据(黑白名单，具体参数参考 requestOriginParser)
     */
    private String limitApp;

    /**
     * 策略枚举
     */
    private AuthorityStrategyEnum authorityStrategyEnum;
}
