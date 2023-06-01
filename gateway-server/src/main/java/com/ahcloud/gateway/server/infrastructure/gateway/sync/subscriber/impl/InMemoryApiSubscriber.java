package com.ahcloud.gateway.server.infrastructure.gateway.sync.subscriber.impl;

import com.ahcloud.gateway.core.application.helper.GatewayApiHelper;
import com.ahcloud.gateway.core.domain.api.dto.ApiDefinitionDTO;
import com.ahcloud.gateway.core.infrastructure.repository.bean.GatewayApi;
import com.ahcloud.gateway.server.infrastructure.gateway.sync.cache.ApiDataCache;
import com.ahcloud.gateway.server.infrastructure.gateway.sync.subscriber.ApiSubscriber;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/5/29 23:24
 **/
@Component
public class InMemoryApiSubscriber implements ApiSubscriber {

    @Override
    public void onSubscribe(GatewayApi gatewayApi) {
        ApiDefinitionDTO apiDefinitionDTO = GatewayApiHelper.convertToDTO(gatewayApi);
        ApiDataCache instance = ApiDataCache.getInstance();
        instance.cache(apiDefinitionDTO);
    }

    @Override
    public void unSubscribe(GatewayApi gatewayApi) {
        ApiDefinitionDTO apiDefinitionDTO = GatewayApiHelper.convertToDTO(gatewayApi);
        ApiDataCache instance = ApiDataCache.getInstance();
        instance.remove(apiDefinitionDTO);
    }

    @Override
    public void refresh(List<GatewayApi> gatewayApiList) {
        for (GatewayApi gatewayApi : gatewayApiList) {
            this.unSubscribe(gatewayApi);
            this.onSubscribe(gatewayApi);
        }
    }
}
