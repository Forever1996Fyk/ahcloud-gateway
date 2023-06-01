package com.ahcloud.gateway.core.application.helper;

import com.ahcloud.gateway.core.domain.api.vo.AppNameSelectVO;
import com.ahcloud.gateway.core.infrastructure.repository.bean.GatewayApiMetaData;
import com.ahcloud.gateway.register.common.dto.MetaDataRegisterDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: ahcloud-gateway
 * @description: 网关接口元数据
 * @author: YuKai Fan
 * @create: 2023/5/29 16:41
 **/
public class GatewayApiMetadataHelper {

    /**
     * 数据转换
     * @param dto
     * @return
     */
    public static GatewayApiMetaData convert(MetaDataRegisterDTO dto) {
        GatewayApiMetaData gatewayApiMetaData = Convert.INSTANCE.convert(dto);
        gatewayApiMetaData.setCreator("SYSTEM");
        gatewayApiMetaData.setModifier("SYSTEM");
        return gatewayApiMetaData;
    }

    public static List<AppNameSelectVO> convertToSelectVOList(List<GatewayApiMetaData> list) {
        return list.stream().map(gatewayApiMetaData ->
                    AppNameSelectVO.builder()
                            .appName(gatewayApiMetaData.getAppName())
                            .env(gatewayApiMetaData.getEnv())
                            .build()
                ).collect(Collectors.toList());
    }

    @Mapper
    public interface Convert {
        Convert INSTANCE = Mappers.getMapper(Convert.class);

        /**
         * 数据转换
         * @param dto
         * @return
         */
        @Mappings({
                @Mapping(target = "httpMethod", source = "apiHttpMethodEnum")
        })
        GatewayApiMetaData convert(MetaDataRegisterDTO dto);
    }
}
