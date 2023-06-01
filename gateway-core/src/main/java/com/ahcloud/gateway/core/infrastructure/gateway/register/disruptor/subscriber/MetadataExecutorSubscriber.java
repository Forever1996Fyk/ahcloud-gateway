package com.ahcloud.gateway.core.infrastructure.gateway.register.disruptor.subscriber;

import com.ahcloud.gateway.core.infrastructure.gateway.register.service.GatewayClientRegisterService;
import com.ahcloud.gateway.register.common.dto.MetaDataRegisterDTO;
import com.ahcloud.gateway.register.common.subsriber.ExecutorTypeSubscriber;
import com.ahcloud.gateway.register.common.type.DataType;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/5/29 10:23
 **/
public class MetadataExecutorSubscriber implements ExecutorTypeSubscriber<MetaDataRegisterDTO> {

    private final Map<String, GatewayClientRegisterService> gatewayClientRegisterServiceMap;

    public MetadataExecutorSubscriber(Map<String, GatewayClientRegisterService> gatewayClientRegisterServiceMap) {
        this.gatewayClientRegisterServiceMap = gatewayClientRegisterServiceMap;
    }

    @Override
    public void executor(Collection<MetaDataRegisterDTO> dataList) {
        dataList.forEach(meta -> {
            Optional.ofNullable(this.gatewayClientRegisterServiceMap.get(meta.getRpcType()))
                    .ifPresent(gatewayClientRegisterService -> {
                        synchronized (gatewayClientRegisterService) {
                            gatewayClientRegisterService.registerMetadata(meta);
                        }
                    });
        });
    }

    @Override
    public DataType getType() {
        return DataType.META_DATA;
    }
}
