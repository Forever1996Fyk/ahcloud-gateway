package com.ahcloud.gateway.server.infrastructure.filter;

import com.ahcloud.common.utils.CollectionUtils;
import com.ahcloud.gateway.core.domain.api.bo.ApiRefreshPatternBO;
import com.ahcloud.gateway.core.domain.api.dto.ApiRefreshDTO;
import com.ahcloud.gateway.core.domain.bo.ApiGatewayBO;
import com.ahcloud.gateway.core.domain.context.GatewayContext;
import com.ahcloud.gateway.server.infrastructure.gateway.factory.GatewayApiCacheFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.http.server.PathContainer;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.Set;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/2/21 09:29
 **/
@Slf4j
@Component
public class ApiPathConvertFilter implements WebFilter, Ordered {

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 1 ;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        Object o = exchange.getAttributes().get(GatewayContext.CACHE_GATEWAY_CONTEXT);
        if (Objects.isNull(o)) {
            return chain.filter(exchange);
        }
        GatewayContext gatewayContext = (GatewayContext) o;
        PathContainer pathContainer = gatewayContext.getPathContainer();
        Set<ApiRefreshPatternBO> values = GatewayApiCacheFactory.getValues();
        if (CollectionUtils.isEmpty(values)) {
            return chain.filter(exchange);
        }
        ApiRefreshPatternBO refreshPatternBO = values.stream().filter(apiRefreshPatternBO -> apiRefreshPatternBO.getPathPattern().matches(pathContainer))
                .findFirst().orElse(null);
        if (Objects.isNull(refreshPatternBO)) {
            return chain.filter(exchange);
        }
        ApiRefreshDTO apiRefreshDTO = refreshPatternBO.getApiRefreshDTO();
        if (Objects.nonNull(apiRefreshDTO)) {
            ApiGatewayBO apiGatewayBO = ApiGatewayBO.builder()
                    .path(apiRefreshDTO.getPath())
                    .apiCode(apiRefreshDTO.getApiCode())
                    .apiStatusEnum(apiRefreshDTO.getApiStatusEnum())
                    .auth(apiRefreshDTO.getAuth())
                    .build();
            gatewayContext.setApiGatewayBO(apiGatewayBO);
        }
        return chain.filter(exchange);
    }
}
