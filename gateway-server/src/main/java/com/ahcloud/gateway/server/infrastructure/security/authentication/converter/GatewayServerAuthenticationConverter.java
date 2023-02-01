package com.ahcloud.gateway.server.infrastructure.security.authentication.converter;

import com.ahcloud.gateway.client.enums.AppPlatformEnum;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import reactor.core.publisher.Mono;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/1/18 09:43
 **/
public interface GatewayServerAuthenticationConverter extends ServerAuthenticationConverter {

    /**
     * app平台类型
     *
     * @return AppPlatformEnum
     */
    Mono<AppPlatformEnum> getAppPlatform();
}
