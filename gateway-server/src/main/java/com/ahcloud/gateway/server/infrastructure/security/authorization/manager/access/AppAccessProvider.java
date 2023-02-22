package com.ahcloud.gateway.server.infrastructure.security.authorization.manager.access;

import com.ahcloud.gateway.client.enums.AppPlatformEnum;
import com.ahcloud.gateway.server.infrastructure.security.authentication.user.AdminOAuth2User;
import com.ahcloud.kernel.core.common.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.Set;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/1/18 20:04
 **/
@Slf4j
@Component
public class AppAccessProvider extends AbstractAccessProvider {

    private final static String LOG_MARK = "AppAccessProvider";

    @Override
    public Mono<AuthorizationDecision> doCheck(Mono<Authentication> authentication, AuthorizationContext context) {
        return authentication
                .filter(Authentication::isAuthenticated)
                .cast(BearerTokenAuthentication.class)
                .map(BearerTokenAuthentication::getPrincipal)
                .cast(AdminOAuth2User.class)
                .filter(Objects::nonNull)
                .map(oAuth2User -> {
                    //注意，因为webflux的响应式编程 不能再采取原先的编码方式 即应该先将gatewayContext放入exchange中，否则其他地方可能取不到
                    context.getExchange().getAttributes().put(Constant.CTX_KEY_USER_ID.toString(), oAuth2User.getUserId());
                    return new AuthorizationDecision(true);
                })
                .defaultIfEmpty(new AuthorizationDecision(false));
    }

    @Override
    protected String getLogMark() {
        return LOG_MARK;
    }

    @Override
    public AppPlatformEnum getAppPlatform() {
        return AppPlatformEnum.APP;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        AccessProviderStrategyFactory.register(AppPlatformEnum.APP, this);
    }
}
