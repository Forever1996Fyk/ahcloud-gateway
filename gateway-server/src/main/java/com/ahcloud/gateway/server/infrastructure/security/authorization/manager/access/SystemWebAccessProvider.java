package com.ahcloud.gateway.server.infrastructure.security.authorization.manager.access;

import com.ahcloud.common.model.KeyValue;
import com.ahcloud.gateway.client.enums.AppPlatformEnum;
import com.ahcloud.gateway.client.enums.GatewayRetCodeEnum;
import com.ahcloud.gateway.server.domain.context.GatewayContext;
import com.ahcloud.gateway.server.infrastructure.exception.GatewayAccessDeniedException;
import com.ahcloud.gateway.server.infrastructure.security.authentication.user.AdminOAuth2User;
import com.ahcloud.gateway.server.infrastructure.security.authorization.authority.bo.AdminUserReactiveAuthorityBo;
import com.ahcloud.gateway.server.infrastructure.security.authorization.service.ReactiveAuthorityService;
import com.ahcloud.gateway.server.infrastructure.util.ServerWebExchangeUtils;
import com.ahcloud.kernel.core.common.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/1/18 20:04
 **/
@Slf4j
@Component
public class SystemWebAccessProvider implements AccessProvider {
    @Resource
    private ReactiveAuthorityService<AdminUserReactiveAuthorityBo> adminReactiveAuthorityService;

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> authentication, AuthorizationContext context) {
        return authentication
                .filter(Authentication::isAuthenticated)
                .cast(BearerTokenAuthentication.class)
                .map(BearerTokenAuthentication::getPrincipal)
                .cast(AdminOAuth2User.class)
                .filter(Objects::nonNull)
                .map(oAuth2User -> {
                    // 这里如果需要可以做权限校验 不满足权限可以抛出 GatewayAccessDeniedException todo
                    //注意，因为webflux的响应式编程 不能再采取原先的编码方式 即应该先将gatewayContext放入exchange中，否则其他地方可能取不到
                    context.getExchange().getAttributes().put(Constant.CTX_KEY_USER_ID.toString(), oAuth2User.getUserId());
                   return new AuthorizationDecision(true);
                }).defaultIfEmpty(new AuthorizationDecision(false));
    }

    @Override
    public AppPlatformEnum getAppPlatform() {
        return AppPlatformEnum.SYSTEM_WEB;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        AccessProviderStrategyFactory.register(AppPlatformEnum.SYSTEM_WEB, this);
    }
}
