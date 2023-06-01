package com.ahcloud.gateway.core.application.manager;

import com.ahcloud.gateway.client.enums.GatewayRetCodeEnum;
import com.ahcloud.gateway.core.application.helper.GatewayApiMetadataHelper;
import com.ahcloud.gateway.core.application.service.GatewayApiMetaDataService;
import com.ahcloud.gateway.core.domain.api.vo.ApiMetadataVO;
import com.ahcloud.gateway.core.domain.api.vo.ApiRegisterVO;
import com.ahcloud.gateway.core.domain.api.vo.AppNameSelectVO;
import com.ahcloud.gateway.core.infrastructure.exception.BizException;
import com.ahcloud.gateway.core.infrastructure.exception.GatewayException;
import com.ahcloud.gateway.core.infrastructure.repository.bean.GatewayApiMetaData;
import com.ahcloud.gateway.core.infrastructure.repository.bean.GatewayApiRegister;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/5/29 17:27
 **/
@Component
public class GatewayApiMetadataManager {
    @Resource
    private NamingService namingService;
    @Resource
    private GatewayApiMetaDataService gatewayApiMetaDataService;

    /**
     * 根据serviceId分组获取serviceId集合
     *
     * @param name
     * @return
     */
    public List<AppNameSelectVO> listServiceIdByName(String name) {
        List<GatewayApiMetaData> list = gatewayApiMetaDataService.list(
                new QueryWrapper<GatewayApiMetaData>().lambda()
                        .select(GatewayApiMetaData::getAppName, GatewayApiMetaData::getEnv)
                        .groupBy(GatewayApiMetaData::getAppName, GatewayApiMetaData::getEnv)
                        .like(
                                StringUtils.isNotBlank(name),
                                GatewayApiMetaData::getAppName,
                                name
                        )
        );
        // 移除没有健康实例的应用
        list.removeIf(gatewayApiMetaData -> hasNoHealthInstance(gatewayApiMetaData.getAppName(), gatewayApiMetaData.getEnv()));
        return GatewayApiMetadataHelper.convertToSelectVOList(list);
    }

    /**
     * 获取api元数据选择列表
     * @param appName
     * @param env
     * @return
     */
    public List<ApiMetadataVO> selectApiMetadataList(String appName, String env) {
        if (StringUtils.isBlank(appName)) {
            throw new BizException(GatewayRetCodeEnum.PARAM_ILLEGAL_FIELD, "appName");
        }
        if (StringUtils.isBlank(env)) {
            throw new BizException(GatewayRetCodeEnum.PARAM_ILLEGAL_FIELD, "env");
        }
        if (hasNoHealthInstance(appName, env)) {
            throw new BizException(GatewayRetCodeEnum.APP_INSTANCE_ERROR, env, appName);
        }
        List<GatewayApiMetaData> list = gatewayApiMetaDataService.list(
                new QueryWrapper<GatewayApiMetaData>().lambda()
                        .eq(GatewayApiMetaData::getAppName, appName)
                        .eq(GatewayApiMetaData::getEnv, env)

        );
        return list.stream()
                .map(gatewayApiMetaData ->
                        ApiMetadataVO.builder()
                                .id(gatewayApiMetaData.getId())
                                .apiPath(gatewayApiMetaData.getApiPath())
                                .appName(gatewayApiMetaData.getAppName())
                                .httpMethod(gatewayApiMetaData.getHttpMethod())
                                .className(gatewayApiMetaData.getQualifiedName() + "." + gatewayApiMetaData.getMethodName())
                                .serviceId(gatewayApiMetaData.getServiceId())
                                .qualifiedName(gatewayApiMetaData.getQualifiedName())
                                .methodName(gatewayApiMetaData.getMethodName())
                                .build()
                )
                .collect(Collectors.toList());
    }

    private boolean hasNoHealthInstance(String appName, String env) {
        try {
            Instance instance = namingService.selectOneHealthyInstance(appName, env);
            return Objects.isNull(instance);
        } catch (NacosException e) {
            return true;
        }
    }

}
