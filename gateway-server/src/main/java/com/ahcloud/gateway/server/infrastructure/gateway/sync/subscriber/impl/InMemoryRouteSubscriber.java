package com.ahcloud.gateway.server.infrastructure.gateway.sync.subscriber.impl;

import com.ahcloud.common.utils.CollectionUtils;
import com.ahcloud.common.utils.JsonUtils;
import com.ahcloud.gateway.core.domain.route.dto.FilterDefinitionDTO;
import com.ahcloud.gateway.core.domain.route.dto.PredicateDefinitionDTO;
import com.ahcloud.gateway.core.infrastructure.repository.bean.GatewayRouteDefinition;
import com.ahcloud.gateway.server.infrastructure.gateway.sync.subscriber.RouteSubscriber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/5/29 22:41
 **/
@Component
public class InMemoryRouteSubscriber implements RouteSubscriber {

    private final RouteDefinitionRepository repository;

    @Autowired
    public InMemoryRouteSubscriber(RouteDefinitionRepository repository) {
        this.repository = repository;
    }

    @Override
    public void onSubscribe(GatewayRouteDefinition gatewayRouteDefinition) {
        RouteDefinition routeDefinition = this.convert(gatewayRouteDefinition);
        repository.save(Mono.just(routeDefinition)).subscribe();
    }

    @Override
    public void unSubscribe(String routeId) {
        repository.delete(Mono.just(routeId)).subscribe();
    }

    @Override
    public void refresh(List<GatewayRouteDefinition> gatewayRouteDefinitionList) {
        gatewayRouteDefinitionList.forEach(gatewayRouteDefinition -> {
            this.unSubscribe(gatewayRouteDefinition.getRouteId());
            this.onSubscribe(gatewayRouteDefinition);
        });
    }

    private RouteDefinition convert(GatewayRouteDefinition gatewayRouteDefinition) {
        RouteDefinition routeDefinition = new RouteDefinition();
        routeDefinition.setId(gatewayRouteDefinition.getRouteId());
        routeDefinition.setOrder(0);
        try {
            routeDefinition.setUri(new URI(gatewayRouteDefinition.getUri()));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        List<FilterDefinitionDTO> filterDefinitionDTOList = JsonUtils.jsonToList(gatewayRouteDefinition.getFilterDefinitionConfig(), FilterDefinitionDTO.class);
        if (CollectionUtils.isNotEmpty(filterDefinitionDTOList)) {
            List<FilterDefinition> filterDefinitionList = filterDefinitionDTOList.stream()
                    .map(filterDefinitionDTO -> {
                        FilterDefinition filterDefinition = new FilterDefinition();
                        filterDefinition.setName(filterDefinitionDTO.getName());
                        filterDefinition.setArgs(filterDefinitionDTO.getArgs());
                        return filterDefinition;
                    }).collect(Collectors.toList());
            routeDefinition.setFilters(filterDefinitionList);
        }
        List<PredicateDefinitionDTO> predicateDefinitionDTOList = JsonUtils.jsonToList(gatewayRouteDefinition.getPredicateDefinitionConfig(), PredicateDefinitionDTO.class);
        if (CollectionUtils.isNotEmpty(predicateDefinitionDTOList)) {
            List<PredicateDefinition> predicateDefinitionList = predicateDefinitionDTOList.stream()
                    .map(predicateDefinitionDTO -> {
                        PredicateDefinition predicateDefinition = new PredicateDefinition();
                        predicateDefinition.setName(predicateDefinitionDTO.getName());
                        predicateDefinition.setArgs(predicateDefinitionDTO.getArgs());
                        return predicateDefinition;
                    }).collect(Collectors.toList());
            routeDefinition.setPredicates(predicateDefinitionList);
        }
        return routeDefinition;
    }
}
