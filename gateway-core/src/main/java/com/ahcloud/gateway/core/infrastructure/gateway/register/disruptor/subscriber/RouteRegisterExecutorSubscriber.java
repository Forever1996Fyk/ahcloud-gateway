package com.ahcloud.gateway.core.infrastructure.gateway.register.disruptor.subscriber;

import com.ahcloud.common.utils.CollectionUtils;
import com.ahcloud.gateway.core.infrastructure.gateway.register.service.GatewayClientRegisterService;
import com.ahcloud.gateway.register.common.dto.RouteRegisterDTO;
import com.ahcloud.gateway.register.common.subsriber.ExecutorTypeSubscriber;
import com.ahcloud.gateway.register.common.type.DataType;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/5/29 15:36
 **/
public class RouteRegisterExecutorSubscriber implements ExecutorTypeSubscriber<RouteRegisterDTO> {
    private final Map<String, GatewayClientRegisterService> gatewayClientRegisterServiceMap;

    public RouteRegisterExecutorSubscriber(Map<String, GatewayClientRegisterService> gatewayClientRegisterServiceMap) {
        this.gatewayClientRegisterServiceMap = gatewayClientRegisterServiceMap;
    }

    @Override
    public void executor(Collection<RouteRegisterDTO> dataList) {
        if (CollectionUtils.isEmpty(dataList)) {
            return;
        }
        final Map<String, List<RouteRegisterDTO>> groupByRpcType = dataList.stream()
                .filter(data -> StringUtils.isNotBlank(data.getRpcType()))
                .collect(Collectors.groupingBy(RouteRegisterDTO::getRpcType));
        for (Map.Entry<String, List<RouteRegisterDTO>> entry : groupByRpcType.entrySet()) {
            String rpcType = entry.getKey();
            Optional.ofNullable(gatewayClientRegisterServiceMap.get(rpcType))
                    .ifPresent(service -> {
                        final List<RouteRegisterDTO> list = entry.getValue();
                        Map<String, RouteRegisterDTO> routeRegisterMap = buildData(list);
                        routeRegisterMap.forEach(service::registerRoute);
                    });
        }
    }

    /**
     * 对于SpringCloud 一般情况下一个应用对应一个路由
     * @param list
     * @return
     */
    private Map<String, RouteRegisterDTO> buildData(List<RouteRegisterDTO> list) {
        Map<String, RouteRegisterDTO> resultMap = new HashMap<>(8);
        for (RouteRegisterDTO dto : list) {
            String contextPath = dto.getContextPath();
            String key = StringUtils.isNotBlank(contextPath) ? contextPath : dto.getAppName();
            if (StringUtils.isNotBlank(key)) {
                if (!resultMap.containsKey(key)) {
                    resultMap.put(key, dto);
                }
            }
        }
        return resultMap;
    }

    @Override
    public DataType getType() {
        return DataType.ROUTE;
    }
}
