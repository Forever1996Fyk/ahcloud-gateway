package com.ahcloud.gateway.core.application.manager;

import com.ahcloud.gateway.core.application.service.GatewayApiRegisterService;
import com.ahcloud.gateway.core.domain.api.bo.ApiRegisterBO;
import com.ahcloud.gateway.core.domain.api.vo.ApiRegisterVO;
import com.ahcloud.gateway.core.infrastructure.repository.bean.GatewayApiRegister;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: ahcloud-gateway
 * @description: 网关接口注册管理
 * @author: YuKai Fan
 * @create: 2023/2/3 09:59
 **/
@Slf4j
@Component
public class GatewayApiRegisterManager {
    @Resource
    private GatewayApiRegisterService gatewayApiRegisterService;

    /**
     * 根据serviceId分组获取serviceId集合
     *
     * @param name
     * @return
     */
    public List<String> listServiceIdByName(String name) {
        List<GatewayApiRegister> list = gatewayApiRegisterService.list(
                new QueryWrapper<GatewayApiRegister>().lambda()
                        .groupBy(GatewayApiRegister::getServiceId)
                        .like(GatewayApiRegister::getServiceId, name)
        );
        return list.stream()
                .map(GatewayApiRegister::getServiceId)
                .collect(Collectors.toList());
    }

    /**
     * 根据serviceId获取api注册列表
     * @param serviceId
     * @return
     */
    public List<ApiRegisterVO> listApiRegisterByServiceId(String serviceId, String name) {
        List<GatewayApiRegister> list = gatewayApiRegisterService.list(
                new QueryWrapper<GatewayApiRegister>().lambda()
                        .eq(GatewayApiRegister::getServiceId, serviceId)
                        .and(
                                wrapper -> wrapper
                                        .like(
                                                StringUtils.isNotBlank(name),
                                                GatewayApiRegister::getQualifiedName,
                                                name
                                        ).or()
                                        .like(
                                                StringUtils.isNotBlank(name),
                                                GatewayApiRegister::getMethodName,
                                                name
                                        )
                        )

        );
        return list.stream()
                .map(gatewayApiRegister ->
                        ApiRegisterVO.builder()
                                .id(gatewayApiRegister.getId())
                                .apiPath(gatewayApiRegister.getApiPath())
                                .httpMethod(gatewayApiRegister.getHttpMethod())
                                .className(gatewayApiRegister.getQualifiedName() + "." + gatewayApiRegister.getMethodName())
                                .serviceId(gatewayApiRegister.getServiceId())
                                .qualifiedName(gatewayApiRegister.getQualifiedName())
                                .methodName(gatewayApiRegister.getMethodName())
                                .build()
                )
                .collect(Collectors.toList());
    }


    /**
     * api接口注册
     *
     * @param apiRegisterBO
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean apiRegister(ApiRegisterBO apiRegisterBO) {
        // 根据serviceId, path, httpMethod, 删除已经注册的api
        gatewayApiRegisterService.deleteApiRegister(apiRegisterBO.getServiceId(), apiRegisterBO.getApiPath(), apiRegisterBO.getApiHttpMethodEnum().getName());
        GatewayApiRegister gatewayApiRegister = convert(apiRegisterBO);
        return gatewayApiRegisterService.save(gatewayApiRegister);
    }


    /**
     * api批量接口注册
     *
     * @param apiRegisterBOList
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean batchApiRegister(List<ApiRegisterBO> apiRegisterBOList) {
        // 根据serviceId, path, httpMethod, 删除已经注册的api
        for (ApiRegisterBO apiRegisterBO : apiRegisterBOList) {
            gatewayApiRegisterService.deleteApiRegister(apiRegisterBO.getServiceId(), apiRegisterBO.getApiPath(), apiRegisterBO.getApiHttpMethodEnum().getName());
        }
        List<GatewayApiRegister> gatewayApiRegisterList = convert(apiRegisterBOList);
        return gatewayApiRegisterService.saveBatch(gatewayApiRegisterList);
    }

    /**
     * api接口注册删除
     * <p>
     * 当某个服务下线时，在下线前需要触发该方法，删除当前服务已注册的所有方法
     *
     * @param serviceId
     * @return
     */
    public Integer deleteApiRegisterByServiceId(String serviceId, String env) {
        // 根据serviceId删除已经注册的api
        return gatewayApiRegisterService.deleteApiRegisterByServiceId(serviceId, env);
    }

    private GatewayApiRegister convert(ApiRegisterBO registerBO) {
        GatewayApiRegister gatewayApiRegister = new GatewayApiRegister();
        gatewayApiRegister.setApiPath(registerBO.getApiPath());
        gatewayApiRegister.setEnv(registerBO.getEnv());
        gatewayApiRegister.setHttpMethod(registerBO.getApiHttpMethodEnum().getName());
        gatewayApiRegister.setDeleted(0L);
        gatewayApiRegister.setMethodName(registerBO.getMethodName());
        gatewayApiRegister.setQualifiedName(registerBO.getQualifiedName());
        gatewayApiRegister.setServiceId(registerBO.getServiceId());
        gatewayApiRegister.setCreator("SYSTEM");
        gatewayApiRegister.setModifier("SYSTEM");
        return gatewayApiRegister;
    }

    private List<GatewayApiRegister> convert(List<ApiRegisterBO> registerBOList) {
        return registerBOList.stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }
}
