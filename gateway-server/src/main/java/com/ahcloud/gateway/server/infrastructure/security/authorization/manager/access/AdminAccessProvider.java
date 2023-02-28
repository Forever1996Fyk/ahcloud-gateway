package com.ahcloud.gateway.server.infrastructure.security.authorization.manager.access;

import com.ahcloud.gateway.client.enums.AppPlatformEnum;
import com.ahcloud.gateway.core.domain.bo.UserInfoBO;
import com.ahcloud.gateway.core.domain.context.GatewayContext;
import com.ahcloud.gateway.server.infrastructure.security.authentication.user.AdminOAuth2User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Objects;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/1/18 20:04
 **/
@Slf4j
@Component
public class AdminAccessProvider extends AbstractAccessProvider {
    private final static String LOG_MARK = "AdminAccessProvider";

    @Override
    public Mono<AuthorizationDecision> doCheck(Mono<Authentication> authentication, AuthorizationContext context) {
        return authentication
                .filter(Authentication::isAuthenticated)
                .cast(BearerTokenAuthentication.class)
                .map(BearerTokenAuthentication::getPrincipal)
                .cast(AdminOAuth2User.class)
                .filter(Objects::nonNull)
                .map(oAuth2User -> {
                    GatewayContext gatewayContext = (GatewayContext) context.getExchange().getAttributes().get(GatewayContext.CACHE_GATEWAY_CONTEXT);
                    // 这里如果需要可以做权限校验 不满足权限可以抛出 GatewayAccessDeniedException todo
                    UserInfoBO userInfoBO = new UserInfoBO();
                    userInfoBO.setUserId(String.valueOf(oAuth2User.getUserId()));
                    userInfoBO.setTenantId(String.valueOf(oAuth2User.getTenantId()));
                    Map<String, Object> attributes = oAuth2User.getAttributes();
                    userInfoBO.setToken(attributes.containsKey("token") ? String.valueOf(attributes.get("token")) : StringUtils.EMPTY);

                    gatewayContext.setUserInfoBO(userInfoBO);
                   return new AuthorizationDecision(true);
                }).defaultIfEmpty(new AuthorizationDecision(false));
    }

    @Override
    protected String getLogMark() {
        return LOG_MARK;
    }

    @Override
    public AppPlatformEnum getAppPlatform() {
        return AppPlatformEnum.ADMIN;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        AccessProviderStrategyFactory.register(AppPlatformEnum.ADMIN, this);
    }
}