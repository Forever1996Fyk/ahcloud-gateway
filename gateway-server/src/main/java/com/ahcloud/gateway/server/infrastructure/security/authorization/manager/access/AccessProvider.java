package com.ahcloud.gateway.server.infrastructure.security.authorization.manager.access;

import com.ahcloud.gateway.client.enums.AppPlatformEnum;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import reactor.core.publisher.Mono;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/1/18 20:02
 **/
public interface AccessProvider extends InitializingBean {

    /**
     * 校验权限
     * @param authentication
     * @param context
     * @return
     */
    Mono<AuthorizationDecision> check(Mono<Authentication> authentication, AuthorizationContext context);

    /**
     * 平台类型
     * @return
     */
    AppPlatformEnum getAppPlatform();
}
