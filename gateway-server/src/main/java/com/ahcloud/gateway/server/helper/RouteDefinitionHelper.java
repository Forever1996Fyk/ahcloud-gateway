package com.ahcloud.gateway.server.helper;

import com.ahcloud.gateway.core.domain.route.dto.FilterDefinitionDTO;
import com.ahcloud.gateway.core.domain.route.dto.PredicateDefinitionDTO;
import com.ahcloud.gateway.core.domain.route.dto.RouteDefinitionDTO;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/2/21 23:33
 **/
public class RouteDefinitionHelper {

    public static RouteDefinition convert(RouteDefinitionDTO routeDefinitionDTO) {
        RouteDefinition routeDefinition = new RouteDefinition();
        routeDefinition.setId(routeDefinitionDTO.getId());
        routeDefinition.setUri(routeDefinitionDTO.getUri());
        routeDefinition.setMetadata(routeDefinitionDTO.getMetadata());
        routeDefinition.setOrder(routeDefinitionDTO.getOrder());

        List<PredicateDefinitionDTO> predicateDTOs = routeDefinitionDTO.getPredicates();
        List<PredicateDefinition> predicates = predicateDTOs.stream()
                .map(predicateDefinitionDTO -> {
                    PredicateDefinition predicateDefinition = new PredicateDefinition();
                    predicateDefinition.setArgs(predicateDefinitionDTO.getArgs());
                    predicateDefinition.setName(predicateDefinitionDTO.getName());
                    return predicateDefinition;
                }).collect(Collectors.toList());

        routeDefinition.setPredicates(predicates);

        List<FilterDefinitionDTO> filterDefinitionDTOS = routeDefinitionDTO.getFilters();
        List<FilterDefinition> filters = filterDefinitionDTOS.stream()
                .map(filterDefinitionDTO -> {
                    FilterDefinition filterDefinition = new FilterDefinition();
                    filterDefinition.setArgs(filterDefinitionDTO.getArgs());
                    filterDefinition.setName(filterDefinitionDTO.getName());
                    return filterDefinition;
                }).collect(Collectors.toList());

        routeDefinition.setFilters(filters);
        return routeDefinition;
    }

    public static List<RouteDefinition> convertToList(List<RouteDefinitionDTO> routeDefinitionDTOS) {
        return routeDefinitionDTOS.stream()
                .map(RouteDefinitionHelper::convert)
                .collect(Collectors.toList());
    }
}
