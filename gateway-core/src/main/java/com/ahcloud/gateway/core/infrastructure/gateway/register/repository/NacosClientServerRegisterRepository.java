package com.ahcloud.gateway.core.infrastructure.gateway.register.repository;

import com.ahcloud.common.utils.CollectionUtils;
import com.ahcloud.common.utils.JsonUtils;
import com.ahcloud.gateway.client.constant.GatewayConstants;
import com.ahcloud.gateway.client.constant.NacosPathConstants;
import com.ahcloud.gateway.client.constant.RegisterPathConstants;
import com.ahcloud.gateway.client.enums.RpcTypeEnum;
import com.ahcloud.gateway.core.infrastructure.gateway.register.disruptor.publisher.GatewayClientServerRegisterPublisher;
import com.ahcloud.gateway.register.common.config.PropertiesConfiguration;
import com.ahcloud.gateway.register.common.dto.MetaDataRegisterDTO;
import com.ahcloud.gateway.register.common.dto.RouteRegisterDTO;
import com.alibaba.nacos.api.config.ConfigFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.listener.NamingEvent;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.Executor;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/5/26 17:21
 **/
@Slf4j
public class NacosClientServerRegisterRepository implements GatewayClientServerRegisterRepository {

    private String group = "DEFAULT_GROUP";
    private ConfigService configService;
    private NamingService namingService;

    private GatewayClientServerRegisterPublisher publisher;

    private final Environment environment;

    private final ConcurrentSkipListSet<String> metadataConfigCache = new ConcurrentSkipListSet<>();
    private final ConcurrentMap<String, ConcurrentSkipListSet<String>> metaServiceCache = new ConcurrentHashMap<>();

    private final ConcurrentMap<String, ConcurrentSkipListSet<String>> routeServiceCache = new ConcurrentHashMap<>();

    public NacosClientServerRegisterRepository(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void close() {
        publisher.close();
    }

    @Override
    public void init(GatewayClientServerRegisterPublisher publisher, PropertiesConfiguration configuration) {
        doInit(publisher, configuration);
        subscribe();
    }

    private void doInit(GatewayClientServerRegisterPublisher publisher, PropertiesConfiguration configuration) {
        this.publisher = publisher;
        Properties nacosProperties = configuration.getProps();
        String discoveryGroup = nacosProperties.getProperty(GatewayConstants.DISCOVERY_GROUP);
        this.group = StringUtils.defaultIfBlank(discoveryGroup, this.group);
//        Properties nacosProperties = new Properties();
//        nacosProperties.put(PropertyKeyConst.SERVER_ADDR, properties.getProperty(PropertyKeyConst.SERVER_ADDR));
//        nacosProperties.put(PropertyKeyConst.NAMESPACE, properties.getProperty(PropertyKeyConst.NAMESPACE));
//        // the nacos authentication username
//        nacosProperties.put(PropertyKeyConst.USERNAME, properties.getProperty(PropertyKeyConst.USERNAME, ""));
//        // the nacos authentication password
//        nacosProperties.put(PropertyKeyConst.PASSWORD, properties.getProperty(PropertyKeyConst.PASSWORD, ""));
//        // access key for namespace
//        nacosProperties.put(PropertyKeyConst.ACCESS_KEY, properties.getProperty(PropertyKeyConst.ACCESS_KEY, ""));
//        // secret key for namespace
//        nacosProperties.put(PropertyKeyConst.SECRET_KEY, properties.getProperty(PropertyKeyConst.SECRET_KEY, ""));

        try {
            this.configService = ConfigFactory.createConfigService(nacosProperties);
            this.namingService = NamingFactory.createNamingService(nacosProperties);
        } catch (NacosException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initJustRoute(GatewayClientServerRegisterPublisher publisher, PropertiesConfiguration configuration) {
        doInit(publisher, configuration);
        subscribeRpcTypeRouteService(RpcTypeEnum.SPRING_CLOUD);
    }

    @Override
    public void initJustMeta(GatewayClientServerRegisterPublisher publisher, PropertiesConfiguration configuration) {
        doInit(publisher, configuration);
        subscribeRpcTypeMetadataService(RpcTypeEnum.SPRING_CLOUD);
    }

    /**
     * 目前只接入SpringCloud
     */
    private void subscribe() {
        subscribeRpcTypeService(RpcTypeEnum.SPRING_CLOUD);
    }

    private void subscribeRpcTypeRouteService(final RpcTypeEnum rpcType) {
        final String serviceName = RegisterPathConstants.buildServiceInstancePath(rpcType.getName());
        Map<String, RouteRegisterDTO> services = Maps.newHashMap();
        try {
            List<Instance> healthyInstances = namingService.selectInstances(serviceName, this.group,true);
            healthyInstances.forEach(healthyInstance -> {
                String contextPath = healthyInstance.getMetadata().get(GatewayConstants.CONTEXT_PATH);
                String routeMetadata = healthyInstance.getMetadata().get(GatewayConstants.ROUTE_META_DATA);
                RouteRegisterDTO routeRegisterDTO = JsonUtils.stringToBean(routeMetadata, RouteRegisterDTO.class);
                if (!services.containsKey(contextPath)) {
                    services.put(contextPath, routeRegisterDTO);
                }
            });
            log.info("subscribe route : {}", serviceName);
            if (RpcTypeEnum.acquireSupportRoutes().contains(rpcType)) {
                services.values().forEach(this::publishRegisterRoute);
            }
            // 刷新元数据
            namingService.subscribe(serviceName, this.group, event -> {
                if (event instanceof NamingEvent) {
                    List<Instance> instances = ((NamingEvent) event).getInstances();
                    instances.forEach(instance -> {
                        String contextPath = instance.getMetadata().get(GatewayConstants.CONTEXT_PATH);
                        routeServiceCache.computeIfAbsent(serviceName, k -> new ConcurrentSkipListSet<>()).add(contextPath);
                    });
                    refreshRouteService(rpcType, serviceName);
                }
            });
        } catch (NacosException e) {
            throw new RuntimeException(e);
        }
    }

    private void refreshRouteService(final RpcTypeEnum rpcType, final String serviceName) {
        Optional.ofNullable(routeServiceCache.get(serviceName)).ifPresent(services -> services.forEach(contextPath -> refreshRoute(contextPath, rpcType, serviceName)));
    }

    private void refreshRoute(final String contextPath, final RpcTypeEnum rpcType, final String serviceName) {
        Map<String, RouteRegisterDTO> services = Maps.newHashMap();
        try {
            List<Instance> healthyInstances = namingService.selectInstances(serviceName, this.group,true);
            healthyInstances.forEach(healthyInstance -> {
                if (StringUtils.equals(contextPath, healthyInstance.getMetadata().get(GatewayConstants.CONTEXT_PATH))) {
                    String routeMetadata = healthyInstance.getMetadata().get(GatewayConstants.ROUTE_META_DATA);
                    RouteRegisterDTO routeRegisterDTO = JsonUtils.stringToBean(routeMetadata, RouteRegisterDTO.class);
                    if (!services.containsKey(contextPath)) {
                        services.put(contextPath, routeRegisterDTO);
                    }
                }
            });
            if (CollectionUtils.isEmpty(services)) {
                RouteRegisterDTO routeRegisterDTO = RouteRegisterDTO.builder()
                        .appName(contextPath)
                        .contextPath(contextPath)
                        .serviceId(contextPath)
                        .rpcType(rpcType.getName())
                        .env(this.environment.getProperty(GatewayConstants.ENV))
                        .build();
                services.put(contextPath, routeRegisterDTO);
            }
            log.info("subscribe route : {}", serviceName);
            if (RpcTypeEnum.acquireSupportRoutes().contains(rpcType)) {
                services.values().forEach(this::publishRegisterRoute);
            }
        } catch (NacosException e) {
            throw new RuntimeException(e);
        }
    }

    private void subscribeRpcTypeMetadataService(final RpcTypeEnum rpcType) {
        final String serviceName = RegisterPathConstants.buildServiceInstancePath(rpcType.getName());
        try {
            List<Instance> healthyInstances = namingService.selectInstances(serviceName, this.group,true);
            healthyInstances.forEach(healthyInstance -> {
                String contextPath = healthyInstance.getMetadata().get(GatewayConstants.CONTEXT_PATH);
                String serviceConfigName = RegisterPathConstants.buildServiceConfigPath(rpcType.getName(), contextPath);
                subscribeMetaData(serviceConfigName);
                metadataConfigCache.add(serviceConfigName);
                metaServiceCache.computeIfAbsent(serviceName, k -> new ConcurrentSkipListSet<>()).add(contextPath);
            });
            // 订阅实例, 刷新元数据
            namingService.subscribe(serviceName, this.group, event -> {
                if (event instanceof NamingEvent) {
                    List<Instance> instances = ((NamingEvent) event).getInstances();
                    instances.forEach(instance -> {
                        String contextPath = instance.getMetadata().get(GatewayConstants.CONTEXT_PATH);
                        metaServiceCache.computeIfAbsent(serviceName, k -> new ConcurrentSkipListSet<>()).add(contextPath);
                    });
                    refreshMetadataService(rpcType, serviceName);
                }
            });
        } catch (NacosException e) {
            throw new RuntimeException(e);
        }
    }

    private void subscribeRpcTypeService(final RpcTypeEnum rpcType) {
        final String serviceName = RegisterPathConstants.buildServiceInstancePath(rpcType.getName());
        try {
            Map<String, RouteRegisterDTO> services = Maps.newHashMap();
            List<Instance> healthyInstances = namingService.selectInstances(serviceName, this.group,true);
            healthyInstances.forEach(healthyInstance -> {
                String contextPath = healthyInstance.getMetadata().get(GatewayConstants.CONTEXT_PATH);
                String serviceConfigName = RegisterPathConstants.buildServiceConfigPath(rpcType.getName(), contextPath);
                subscribeMetaData(serviceConfigName);
                metadataConfigCache.add(serviceConfigName);
                String routeMetadata = healthyInstance.getMetadata().get(GatewayConstants.ROUTE_META_DATA);
                RouteRegisterDTO routeRegisterDTO = JsonUtils.stringToBean(routeMetadata, RouteRegisterDTO.class);
                if (!services.containsKey(contextPath)) {
                    services.put(contextPath, routeRegisterDTO);
                }
                metaServiceCache.computeIfAbsent(serviceName, k -> new ConcurrentSkipListSet<>()).add(contextPath);
            });
            if (RpcTypeEnum.acquireSupportRoutes().contains(rpcType)) {
                services.values().forEach(this::publishRegisterRoute);
            }
            log.info("subscribe route : {}", serviceName);
            // 刷新元数据
            namingService.subscribe(serviceName, this.group, event -> {
                if (event instanceof NamingEvent) {
                    List<Instance> instances = ((NamingEvent) event).getInstances();
                    instances.forEach(instance -> {
                        String contextPath = instance.getMetadata().get(GatewayConstants.CONTEXT_PATH);
                        metaServiceCache.computeIfAbsent(serviceName, k -> new ConcurrentSkipListSet<>()).add(contextPath);
                    });
                    refreshMetadataService(rpcType, serviceName);
                }
            });
        } catch (NacosException e) {
            throw new RuntimeException(e);
        }
    }

    private void subscribeMetaData(final String serviceConfigName) {
        String content = readData(serviceConfigName);
        if (StringUtils.isEmpty(content)) {
            return;
        }
        registerMetadata(content);
        log.info("subscribe metadata: {}", serviceConfigName);
        try {
            // 添加监听器，监听配置变动
            configService.addListener(serviceConfigName, NacosPathConstants.GROUP, new Listener() {
                @Override
                public Executor getExecutor() {
                    return null;
                }

                @Override
                public void receiveConfigInfo(final String config) {
                    registerMetadata(config);
                }
            });
        } catch (NacosException e) {
            throw new RuntimeException(e);
        }

    }

    private void registerMetadata(final String metadataConfig) {
        List<String> metadataList = JsonUtils.jsonToList(metadataConfig, String.class);
        metadataList.forEach(this::publishMetadata);
    }

    private void publishMetadata(final String data) {
        log.info("publish metadata: {}", data);
        publisher.publish(Lists.newArrayList(JsonUtils.stringToBean(data, MetaDataRegisterDTO.class)));
    }

    private void refreshMetadataService(final RpcTypeEnum rpcType, final String serviceName) {
        Optional.ofNullable(metaServiceCache.get(serviceName)).ifPresent(services -> services.forEach(contextPath -> registerMetadata(contextPath, serviceName, rpcType)));
    }

    private void registerMetadata(final String contextPath, final String serviceName, final RpcTypeEnum rpcType) {
        try {
            List<Instance> healthyInstances = namingService.selectInstances(serviceName,  this.group, true);
            healthyInstances.forEach(healthyInstance -> {
                if (contextPath.equals(healthyInstance.getMetadata().get(GatewayConstants.CONTEXT_PATH))) {
                    String serviceConfigName = RegisterPathConstants.buildServiceConfigPath(rpcType.getName(), contextPath);
                    if (!metadataConfigCache.contains(serviceConfigName)) {
                        subscribeMetaData(serviceConfigName);
                        metadataConfigCache.add(serviceConfigName);
                    }
                }
            });
        } catch (NacosException e) {
            throw new RuntimeException(e);
        }
    }

    private void publishRegisterRoute(final RouteRegisterDTO routeRegisterDTO) {
        log.info("publish route: {}", routeRegisterDTO);
        publisher.publish(routeRegisterDTO);
    }

    private String readData(final String configName) {
        try {
            return configService.getConfig(configName, NacosPathConstants.GROUP, 5000);
        } catch (NacosException e) {
            throw new RuntimeException(e);
        }
    }
}
