package com.ahcloud.gateway.server.infrastructure.filter;

import com.ahcloud.common.utils.CollectionUtils;
import com.ahcloud.gateway.client.enums.ApiStatusEnum;
import com.ahcloud.gateway.client.enums.GatewayRetCodeEnum;
import com.ahcloud.gateway.core.domain.api.bo.ApiRefreshPatternDTO;
import com.ahcloud.gateway.core.domain.api.dto.ApiDefinitionDTO;
import com.ahcloud.gateway.core.domain.dto.ApiGatewayDTO;
import com.ahcloud.gateway.core.domain.context.GatewayContext;
import com.ahcloud.gateway.core.infrastructure.constant.EnvConstants;
import com.ahcloud.gateway.core.infrastructure.config.GatewayConfiguration;
import com.ahcloud.gateway.server.infrastructure.exception.GatewayException;
import com.ahcloud.gateway.server.infrastructure.gateway.factory.GatewayApiCacheFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final GatewayConfiguration gatewayConfiguration;

    @Autowired
    public ApiPathConvertFilter(GatewayConfiguration gatewayConfiguration) {
        this.gatewayConfiguration = gatewayConfiguration;
    }

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
        Set<ApiRefreshPatternDTO> values = GatewayApiCacheFactory.getValues();
        if (CollectionUtils.isEmpty(values)) {
            return chain.filter(exchange);
        }
        ApiRefreshPatternDTO refreshPatternBO = values.stream().filter(apiRefreshPatternDTO -> apiRefreshPatternDTO.getPathPattern().matches(pathContainer))
                .findFirst().orElse(null);
        if (Objects.isNull(refreshPatternBO)) {
            return chain.filter(exchange);
        }
        ApiDefinitionDTO apiDefinitionDTO = refreshPatternBO.getApiDefinitionDTO();
        if (Objects.isNull(apiDefinitionDTO)) {
            throw new GatewayException(GatewayRetCodeEnum.GATEWAY_API_NOT_EXISTED);
        }
        // 校验api状态
        checkApiStatus(apiDefinitionDTO);
        ApiGatewayDTO apiGatewayDTO = ApiGatewayDTO.builder()
                .path(apiDefinitionDTO.getPath())
                .apiCode(apiDefinitionDTO.getApiCode())
                .dev(apiDefinitionDTO.getDev())
                .test(apiDefinitionDTO.getTest())
                .sit(apiDefinitionDTO.getSit())
                .pre(apiDefinitionDTO.getPre())
                .prod(apiDefinitionDTO.getProd())
                .auth(apiDefinitionDTO.getAuth())
                .build();
        gatewayContext.setApiGatewayDTO(apiGatewayDTO);
        return chain.filter(exchange);
    }

    private void checkApiStatus(ApiDefinitionDTO apiDefinitionDTO) {
        String env = gatewayConfiguration.getEnv();
        Integer status = 0;
        if (EnvConstants.isDev(env)) {
            status = apiDefinitionDTO.getDev();
        } else if (EnvConstants.isTest(env)) {
            status = apiDefinitionDTO.getTest();
        } else if (EnvConstants.isSit(env)) {
            status = apiDefinitionDTO.getSit();
        } else if (EnvConstants.isPre(env)) {
            status = apiDefinitionDTO.getPre();
        } else if (EnvConstants.isProd(env)) {
            status = apiDefinitionDTO.getProd();
        }
        ApiStatusEnum apiStatusEnum = ApiStatusEnum.valueOf(status);
        if (Objects.equals(apiStatusEnum, ApiStatusEnum.OFFLINE)) {
            throw new GatewayException(GatewayRetCodeEnum.GATEWAY_API_OFFLINE);
        }
        if (Objects.equals(apiStatusEnum, ApiStatusEnum.DISABLED)) {
            throw new GatewayException(GatewayRetCodeEnum.GATEWAY_API_DISABLED);
        }
    }
}
