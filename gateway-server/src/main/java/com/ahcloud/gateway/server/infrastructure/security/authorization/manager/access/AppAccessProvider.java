package com.ahcloud.gateway.server.infrastructure.security.authorization.manager.access;

import com.ahcloud.gateway.client.enums.AppPlatformEnum;
import com.ahcloud.gateway.core.domain.dto.UserInfoDTO;
import com.ahcloud.gateway.core.domain.context.GatewayContext;
import com.ahcloud.gateway.server.infrastructure.security.authentication.user.AppOAuth2User;
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
public class AppAccessProvider extends AbstractAccessProvider {

    private final static String LOG_MARK = "AppAccessProvider";

    @Override
    public Mono<AuthorizationDecision> doCheck(Mono<Authentication> authentication, AuthorizationContext context) {
        return authentication
                .filter(Authentication::isAuthenticated)
                .cast(BearerTokenAuthentication.class)
                .map(BearerTokenAuthentication::getPrincipal)
                .cast(AppOAuth2User.class)
                .filter(Objects::nonNull)
                .map(oAuth2User -> {
                    GatewayContext gatewayContext = (GatewayContext) context.getExchange().getAttributes().get(GatewayContext.CACHE_GATEWAY_CONTEXT);
                    UserInfoDTO userInfoDTO = new UserInfoDTO();
                    userInfoDTO.setUserId(String.valueOf(oAuth2User.getUserId()));
                    userInfoDTO.setTenantId(oAuth2User.getTenantId());

                    userInfoDTO.setNickName(oAuth2User.getNickName());
                    userInfoDTO.setUserName(oAuth2User.getName());
                    Map<String, Object> attributes = oAuth2User.getAttributes();
                    userInfoDTO.setToken(attributes.containsKey("token") ? String.valueOf(attributes.get("token")) : StringUtils.EMPTY);

                    gatewayContext.setUserInfoDTO(userInfoDTO);
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
