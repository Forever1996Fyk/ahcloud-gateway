package com.ahcloud.gateway.server.application.service.impl;

import com.ahcloud.gateway.server.infrastructure.repository.bean.GatewayApiRegister;
import com.ahcloud.gateway.server.infrastructure.repository.mapper.GatewayApiRegisterMapper;
import com.ahcloud.gateway.server.application.service.GatewayApiRegisterService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 网关接口注册表 服务实现类
 * </p>
 *
 * @author auto_generation
 * @since 2023-02-03
 */
@Service
public class GatewayApiRegisterServiceImpl extends ServiceImpl<GatewayApiRegisterMapper, GatewayApiRegister> implements GatewayApiRegisterService {

    @Override
    public int deleteApiRegister(String serviceId, String path, String httpMethod) {
        return baseMapper.delete(
                new QueryWrapper<GatewayApiRegister>().lambda()
                        .eq(GatewayApiRegister::getServiceId, serviceId)
                        .eq(GatewayApiRegister::getApiPath, path)
                        .eq(GatewayApiRegister::getHttpMethod, httpMethod)
        );
    }

    @Override
    public int deleteApiRegisterByServiceId(String serviceId) {
        return baseMapper.delete(
                new QueryWrapper<GatewayApiRegister>().lambda()
                        .eq(GatewayApiRegister::getServiceId, serviceId)
        );
    }
}
