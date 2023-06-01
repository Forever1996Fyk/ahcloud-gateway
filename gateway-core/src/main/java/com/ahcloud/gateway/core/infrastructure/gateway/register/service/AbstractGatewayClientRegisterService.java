package com.ahcloud.gateway.core.infrastructure.gateway.register.service;

import com.ahcloud.gateway.core.application.helper.GatewayRouteHelper;
import com.ahcloud.gateway.core.domain.route.dto.FilterDefinitionDTO;
import com.ahcloud.gateway.core.domain.route.dto.PredicateDefinitionDTO;
import com.ahcloud.gateway.core.domain.route.dto.RouteDefinitionDTO;
import com.ahcloud.gateway.register.common.dto.MetaDataRegisterDTO;
import com.ahcloud.gateway.register.common.dto.RouteRegisterDTO;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/5/29 10:36
 **/
public abstract class AbstractGatewayClientRegisterService implements GatewayClientRegisterService {

    @Override
    public void registerMetadata(MetaDataRegisterDTO metaDataRegisterDTO) {
        doRegisterMetadata(metaDataRegisterDTO);
    }

    @Override
    public void registerRoute(String name, RouteRegisterDTO registerDTO) {
        RouteDefinitionDTO routeDefinitionDTO = buildRoute(registerDTO);
        doRegisterRoute(routeDefinitionDTO);
    }

    /**
     * 执行注册路由
     * @param routeDefinitionDTO
     */
    protected abstract void doRegisterRoute(RouteDefinitionDTO routeDefinitionDTO);

    /**
     * 执行注册元数据
     * @param metaDataRegisterDTO
     */
    protected abstract void doRegisterMetadata(MetaDataRegisterDTO metaDataRegisterDTO);

    protected RouteDefinitionDTO buildRoute(RouteRegisterDTO registerDTO) {
        // 设置predicates
        List<PredicateDefinitionDTO> predicates = Lists.newArrayList();
        //源代码中就是这么定义的在PredicateDefinition类中的低35行代码
        String generateName = GatewayRouteHelper.generateName(0);
        Map<String, String> args = Maps.newHashMap();
        args.put(generateName, GatewayRouteHelper.generatePath(registerDTO.getServiceId()));
        PredicateDefinitionDTO predicateDefinition = PredicateDefinitionDTO.builder()
                .name("Path")
                .args(args)
                .build();
        //Path 后的内容可以是多个路径，多个路径用逗号隔开
        predicates.add(predicateDefinition);

        // 设置filters
        List<FilterDefinitionDTO> filters = Lists.newArrayList();
        Map<String, String> argsFilter = Maps.newHashMapWithExpectedSize(20);
        String generateFilterKey = GatewayRouteHelper.generateName(0);
        argsFilter.put(generateFilterKey, "1");
        FilterDefinitionDTO filterDefinition = FilterDefinitionDTO.builder()
                .name("StripPrefix")
                .args(argsFilter)
                .build();
        filters.add(filterDefinition);

        URI uri;
        try {
            uri = new URI("lb://" + registerDTO.getServiceId());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return RouteDefinitionDTO.builder()
                .id(registerDTO.getServiceId())
                .rpcType(registerDTO.getRpcType())
                .env(registerDTO.getEnv())
                .appName(registerDTO.getAppName())
                .serviceId(StringUtils.defaultIfBlank(registerDTO.getServiceId(), registerDTO.getAppName()))
                .contextPath(registerDTO.getContextPath())
                .uri(uri)
                .predicates(predicates)
                .filters(filters)
                .order(0)
                .metadata(new HashMap<>(2))
                .build();
    }
}
