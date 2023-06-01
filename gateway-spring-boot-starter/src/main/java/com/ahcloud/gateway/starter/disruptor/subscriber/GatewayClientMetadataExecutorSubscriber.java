package com.ahcloud.gateway.starter.disruptor.subscriber;

import com.ahcloud.gateway.register.common.dto.MetaDataRegisterDTO;
import com.ahcloud.gateway.register.common.subsriber.ExecutorTypeSubscriber;
import com.ahcloud.gateway.register.common.type.DataType;
import com.ahcloud.gateway.starter.repository.GatewayClientRegisterRepository;

import java.util.Collection;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/5/26 10:26
 **/
public class GatewayClientMetadataExecutorSubscriber implements ExecutorTypeSubscriber<MetaDataRegisterDTO> {

    private final GatewayClientRegisterRepository repository;

    public GatewayClientMetadataExecutorSubscriber(GatewayClientRegisterRepository repository) {
        this.repository = repository;
    }


    @Override
    public void executor(Collection<MetaDataRegisterDTO> dataList) {
        for (MetaDataRegisterDTO metaDataRegisterDTO : dataList) {
            repository.persistMetaData(metaDataRegisterDTO);
        }
    }

    @Override
    public DataType getType() {
        return DataType.META_DATA;
    }
}
