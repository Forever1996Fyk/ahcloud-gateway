package com.ahcloud.gateway.core.application.helper;

import com.ahcloud.gateway.core.domain.route.dto.FilterDefinitionDTO;
import com.ahcloud.gateway.core.domain.route.dto.PredicateDefinitionDTO;
import com.ahcloud.gateway.core.domain.route.dto.RouteDefinitionDTO;
import com.ahcloud.gateway.core.infrastructure.repository.bean.GatewayApi;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/2/6 10:44
 **/
public class RouteHelper {

    /**
     * 构建路由定义
     * @param gatewayApi
     * @return
     * @throws URISyntaxException
     */
    public static RouteDefinitionDTO buildRoute(GatewayApi gatewayApi) {
        return buildRoute(gatewayApi.getServiceId());
    }

    public static RouteDefinitionDTO buildRoute(String serviceId) {
        // 设置predicates
        List<PredicateDefinitionDTO> predicates = Lists.newArrayList();
        //源代码中就是这么定义的在PredicateDefinition类中的低35行代码
        String generateName = generateName(0);
        Map<String, String> args = Maps.newHashMap();
        args.put(generateName, generatePath(serviceId));
        PredicateDefinitionDTO predicateDefinition = PredicateDefinitionDTO.builder()
                .name("Path")
                .args(args)
                .build();
        //Path 后的内容可以是多个路径，多个路径用逗号隔开
        predicates.add(predicateDefinition);

        // 设置filters
        List<FilterDefinitionDTO> filters = Lists.newArrayList();
        Map<String, String> argsFilter = Maps.newHashMapWithExpectedSize(20);
        String generateFilterKey = generateName(0);
        argsFilter.put(generateFilterKey, "1");
        FilterDefinitionDTO filterDefinition = FilterDefinitionDTO.builder()
                .name("StripPrefix")
                .args(argsFilter)
                .build();
        filters.add(filterDefinition);

        URI uri;
        try {
            uri = new URI("lb://" + serviceId);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return RouteDefinitionDTO.builder()
                .id(serviceId)
                .uri(uri)
                .predicates(predicates)
                .filters(filters)
                .order(0)
                .metadata(new HashMap<>(2))
                .build();
    }

    public static List<RouteDefinitionDTO> buildRoutes(List<GatewayApi> gatewayApiList) {
        return gatewayApiList.stream()
                .map(RouteHelper::buildRoute)
                .collect(Collectors.toList());
    }

    /**
     * 生成路由路径
     *
     * @param serviceId
     * @return
     */
    public static String generatePath(String serviceId) {
        return "/" + serviceId + "/**";
    }

    public static final String GENERATED_NAME_PREFIX = "_genkey_";

    public static String generateName(int i) {
        return GENERATED_NAME_PREFIX + i;
    }

}
