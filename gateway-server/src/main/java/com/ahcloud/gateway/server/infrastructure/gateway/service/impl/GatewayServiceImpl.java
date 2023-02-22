package com.ahcloud.gateway.server.infrastructure.gateway.service.impl;

import com.ahcloud.common.utils.CollectionUtils;
import com.ahcloud.common.utils.JsonUtils;
import com.ahcloud.gateway.server.infrastructure.gateway.service.GatewayService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/2/6 10:27
 **/
@Slf4j
@Component
public class GatewayServiceImpl implements GatewayService, ApplicationEventPublisherAware, ApplicationRunner {

    private final RouteDefinitionRepository routeDefinitionRepository;

    private ApplicationEventPublisher applicationEventPublisher;

    public GatewayServiceImpl(RouteDefinitionRepository routeDefinitionRepository) {
        this.routeDefinitionRepository = routeDefinitionRepository;
    }

    @Override
    public Mono<Boolean> existedRoute(String serviceId) {
        Flux<RouteDefinition> routeDefinitions = routeDefinitionRepository.getRouteDefinitions();
        return routeDefinitions
                .any(routeDefinition -> StringUtils.equals(routeDefinition.getId(), serviceId));
    }

    @Override
    public Mono<Boolean> addRoute(RouteDefinition routeDefinition) {
        if (Objects.isNull(routeDefinition)) {
            log.error("路由为空, 添加失败");
            return Mono.just(false);
        }
        return existedRoute(routeDefinition.getId())
                .filter(item -> !item)
                .map(item -> {
                    routeDefinitionRepository.save(Mono.just(routeDefinition)).subscribe();
                    this.applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));
                    log.info("网关路由服务刷新缓存, routeDefinition is {}", JsonUtils.toJsonString(routeDefinition));
                    return true;
                })
                .defaultIfEmpty(false);
    }

    @Override
    public Mono<Boolean> batchAddRoute(List<RouteDefinition> routeDefinitionList) {
        if (CollectionUtils.isEmpty(routeDefinitionList)) {
            log.error("路由为空, 添加失败");
            return Mono.just(false);
        }
       return Flux.fromIterable(routeDefinitionList)
                        .flatMap(routeDefinition -> existedRoute(routeDefinition.getId())
                                .filter(item -> item)
                                .map(item -> {
                                    routeDefinitionRepository.save(Mono.just(routeDefinition)).subscribe();
                                    return true;
                                }).defaultIfEmpty(false)
                        ).next()
                        .defaultIfEmpty(false)
                        .filter(item -> item)
                        .map(item -> {
                            this.applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));
                            return true;
                        }).defaultIfEmpty(false);
    }

    @Override
    public boolean batchDeleteRoute(List<String> serviceIdList) {
        if (CollectionUtils.isEmpty(serviceIdList)) {
            log.error("路由id为空, 删除失败");
            return false;
        }
        for (String serviceId : serviceIdList) {
            routeDefinitionRepository.delete(Mono.just(serviceId)).subscribe();
        }
        this.applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));
        return true;
    }

    @Override
    public Mono<Boolean> deleteRoute(String serviceId) {
        if (StringUtils.isBlank(serviceId)) {
            log.error("路由id为空, 删除失败");
            return Mono.just(false);
        }
        return existedRoute(serviceId)
                .filter(item -> item)
                .map(item -> {
                    routeDefinitionRepository.delete(Mono.just(serviceId)).subscribe();
                    this.applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));
                    return true;
                })
                .defaultIfEmpty(false);
    }

    @Override
    public Mono<Boolean> updateRoute(RouteDefinition routeDefinition) {
        if (Objects.isNull(routeDefinition)) {
            log.error("路由为空, 更新失败");
            return Mono.just(false);
        }
        return existedRoute(routeDefinition.getId())
                .filter(item -> item)
                .map(item -> {
                    routeDefinitionRepository.delete(Mono.just(routeDefinition.getId())).subscribe();
                    routeDefinitionRepository.save(Mono.just(routeDefinition)).subscribe();
                    this.applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));
                    return true;
                })
                .defaultIfEmpty(false);
    }

    @Override
    public Mono<Void> refreshRoute(List<RouteDefinition> routeDefinitionList) {
        Flux<RouteDefinition> routeDefinitions = routeDefinitionRepository.getRouteDefinitions();
        return routeDefinitions
                .flatMap(routeDefinition -> routeDefinitionRepository.delete(Mono.just(routeDefinition.getId())))
                .then(Mono.defer(() ->
                        Flux.fromIterable(routeDefinitionList)
                                .flatMap(routeDefinition -> {
                                    return routeDefinitionRepository.save(Mono.just(routeDefinition));
                                })
                                .then(Mono.defer(() -> {
                                    this.applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));
                                    return Mono.empty();
                                })))
                );
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
//        List<GatewayApi> gatewayApiList = gatewayApiService.list(
//                new QueryWrapper<GatewayApi>().lambda()
//                        .select(GatewayApi::getServiceId)
//                        .groupBy(GatewayApi::getServiceId)
//                        .eq(GatewayApi::getStatus, ApiStatusEnum.NORMAL.getStatus())
//                        .eq(GatewayApi::getDeleted, DeletedEnum.NO.value)
//        );
//        gatewayApiList.stream()
//                .map(RouteHelper::buildRoute)
//                .forEach(routeDefinition -> {
//                    routeDefinitionRepository.save(Mono.just(routeDefinition)).subscribe();
//                    this.applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));
//                });
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}
