package com.ahcloud.gateway.starter.repository;

import com.ahcloud.common.utils.JsonUtils;
import com.ahcloud.gateway.client.constant.GatewayConstants;
import com.ahcloud.gateway.client.constant.NacosPathConstants;
import com.ahcloud.gateway.client.constant.RegisterPathConstants;
import com.ahcloud.gateway.client.util.ContextPathUtils;
import com.ahcloud.gateway.register.common.config.PropertiesConfiguration;
import com.ahcloud.gateway.register.common.dto.MetaDataRegisterDTO;
import com.ahcloud.gateway.register.common.dto.RouteRegisterDTO;
import com.alibaba.nacos.api.config.ConfigFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.ConfigType;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/5/26 09:20
 **/
@Slf4j
public class GatewayClientNacosRegisterRepository implements GatewayClientRegisterRepository {
    private ConfigService configService;

    private NamingService namingService;

    private String group;

    private final ConcurrentLinkedQueue<String> metadataCache = new ConcurrentLinkedQueue<>();

    @Override
    public void init(PropertiesConfiguration config) {
        Properties nacosProperties = config.getProps();
        this.group = nacosProperties.getProperty(GatewayConstants.DISCOVERY_GROUP);
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
    public void close() {
        try {
            configService.shutDown();
            namingService.shutDown();
        } catch (NacosException e) {
            log.error("GatewayClientNacosRegisterRepository close error!", e);
        }
    }

    @Override
    public void persistRoute(RouteRegisterDTO registerDTO) {
        String rpcType = registerDTO.getRpcType();
        String contextPath = ContextPathUtils.buildRealNode(registerDTO.getContextPath(), registerDTO.getAppName());
        String host = registerDTO.getHost();
        Integer port = registerDTO.getPort();
        registerService(rpcType, contextPath, host, port, registerDTO);
    }

    private synchronized void registerService(final String rpcType,
                                              final String contextPath,
                                              final String host,
                                              final int port,
                                              final RouteRegisterDTO registerDTO) {
        Instance instance = new Instance();
        instance.setEphemeral(true);
        instance.setIp(host);
        instance.setPort(port);
        Map<String, String> metadataMap = Maps.newHashMap();
        metadataMap.put(GatewayConstants.CONTEXT_PATH, contextPath);
        metadataMap.put(GatewayConstants.ROUTE_META_DATA, JsonUtils.toJsonString(registerDTO));
        instance.setMetadata(metadataMap);
        String serviceName = RegisterPathConstants.buildServiceInstancePath(rpcType);
        try {
            namingService.registerInstance(serviceName, this.group, instance);
        } catch (NacosException e) {
            throw new RuntimeException(e);
        }
        log.info("register service route success : {}", serviceName);
    }

    @Override
    public void persistMetaData(final MetaDataRegisterDTO metadata) {
        String rpcType = metadata.getRpcType();
        String contextPath = ContextPathUtils.buildRealNode(metadata.getContextPath(), metadata.getAppName());
        registerConfig(rpcType, contextPath, metadata);
    }

    private synchronized void registerConfig(final String rpcType, final String contextPath, final MetaDataRegisterDTO metaData) {
        metadataCache.add(JsonUtils.toJsonString(metaData));
        String configName = RegisterPathConstants.buildServiceConfigPath(rpcType, contextPath);
        try {
            final String defaultGroup = NacosPathConstants.GROUP;
            if (configService.publishConfig(configName,
                    defaultGroup,
                    JsonUtils.toJsonString(metadataCache),
                    ConfigType.JSON.getType())) {
                log.info("register metadata success: {}", metaData.getQualifiedName());
            } else {
                throw new RuntimeException("register metadata fail , please check ");
            }
        } catch (NacosException e) {
            throw new RuntimeException(e);
        }
    }
}
