package com.ahcloud.gateway.server.infrastructure.security.authorization.manager.access;

import com.ahcloud.gateway.client.enums.AppPlatformEnum;
import com.ahcloud.gateway.server.infrastructure.security.authentication.user.AdminOAuth2User;
import com.ahcloud.gateway.server.infrastructure.security.authorization.authority.bo.AdminUserReactiveAuthorityBo;
import com.ahcloud.gateway.server.infrastructure.security.authorization.service.ReactiveAuthorityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Map;

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
        String path = context.getExchange().getRequest().getURI().getPath();
        // 根据uri获取 权限标识
        return authentication
                .filter(Authentication::isAuthenticated)
                .cast(BearerTokenAuthentication.class)
                .map(BearerTokenAuthentication::getPrincipal)
                .cast(AdminOAuth2User.class)
                .map(oAuth2User -> new AuthorizationDecision(
                        adminReactiveAuthorityService.process(
                                oAuth2User.getUserId(),
                                oAuth2User.getTenantId(),
                                path,
                                oAuth2User.getAuthorities()
                        )
                ))
                .defaultIfEmpty(new AuthorizationDecision(false));
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
