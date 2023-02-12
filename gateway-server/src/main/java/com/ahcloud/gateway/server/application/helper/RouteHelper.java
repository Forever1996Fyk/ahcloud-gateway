package com.ahcloud.gateway.server.application.helper;

import com.ahcloud.gateway.server.infrastructure.repository.bean.GatewayApi;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.support.NameUtils;

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
    public static RouteDefinition buildRoute(GatewayApi gatewayApi) {
        return buildRoute(gatewayApi.getServiceId());
    }

    public static RouteDefinition buildRoute(String serviceId) {
        RouteDefinition routeDefinition = new RouteDefinition();
        routeDefinition.setId(serviceId);
        try {
            routeDefinition.setUri(new URI("lb://" + serviceId));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        // 设置predicates
        List<PredicateDefinition> predicates = Lists.newArrayList();
        PredicateDefinition predicateDefinition = new PredicateDefinition();
        //Path 后的内容可以是多个路径，多个路径用逗号隔开
        predicateDefinition.setName("Path");
        //源代码中就是这么定义的在PredicateDefinition类中的低35行代码
        String generateName = NameUtils.generateName(0);
        Map<String, String> args = Maps.newHashMap();
        args.put(generateName, generatePath(serviceId));
        predicateDefinition.setArgs(args);
        predicates.add(predicateDefinition);

        // 设置filters
        List<FilterDefinition> filters = Lists.newArrayList();
        FilterDefinition filterDefinition = new FilterDefinition();
        filterDefinition.setName("StripPrefix");
        Map<String, String> argsFilter = Maps.newHashMapWithExpectedSize(20);
        String generateFilterKey = NameUtils.generateName(0);
        argsFilter.put(generateFilterKey, "2");
        filterDefinition.setArgs(argsFilter);
        filters.add(filterDefinition);

        routeDefinition.setPredicates(predicates);
        routeDefinition.setFilters(filters);

        return routeDefinition;
    }

    public static List<RouteDefinition> buildRoutes(List<GatewayApi> gatewayApiList) {
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
}
