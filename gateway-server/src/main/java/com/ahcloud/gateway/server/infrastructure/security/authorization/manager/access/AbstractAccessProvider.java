package com.ahcloud.gateway.server.infrastructure.security.authorization.manager.access;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import reactor.core.publisher.Mono;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/2/16 10:56
 **/
@Slf4j
public abstract class AbstractAccessProvider implements AccessProvider {

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> authentication, AuthorizationContext context) {
        log.info("{}[check] start authorization", getLogMark());
        return doCheck(authentication, context);
    }

    /**
     * 执行校验
     * @param authentication 认证信息
     * @param context 请求上下文
     * @return
     */
    protected abstract Mono<AuthorizationDecision> doCheck(Mono<Authentication> authentication, AuthorizationContext context);

    /**
     * 日志标识
     * @return
     */
    protected abstract String getLogMark();
}
