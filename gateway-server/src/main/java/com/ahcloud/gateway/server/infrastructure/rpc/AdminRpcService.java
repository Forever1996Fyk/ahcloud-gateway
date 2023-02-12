package com.ahcloud.gateway.server.infrastructure.rpc;

import com.ahcloud.admin.client.domain.dubbo.base.BaseAuthorityDTO;
import com.ahcloud.admin.client.domain.dubbo.base.query.BaseAuthorityPageQueryDTO;
import com.ahcloud.admin.client.domain.dubbo.token.AdminUserAuthenticationDTO;
import com.ahcloud.admin.dubbo.authentication.TokenAuthenticationDubboService;
import com.ahcloud.admin.dubbo.base.AdminBaseDubboService;
import com.ahcloud.common.result.RpcResult;
import com.ahcloud.common.result.page.RpcPageResult;
import com.ahcloud.gateway.client.enums.GatewayRetCodeEnum;
import com.ahcloud.gateway.server.application.helper.AdminUserAuthenticationHelper;
import com.ahcloud.gateway.server.domain.admin.bo.AdminAuthorityBO;
import com.ahcloud.gateway.server.domain.admin.bo.AdminUserAuthenticationBO;
import com.ahcloud.gateway.server.domain.admin.query.AdminAuthorityQuery;
import com.ahcloud.gateway.server.infrastructure.exception.BizException;
import com.ahcloud.gateway.server.infrastructure.exception.GatewayException;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @program: ahcloud-gateway
 * @description: admin rpc服务
 * @author: YuKai Fan
 * @create: 2023/1/31 16:28
 **/
@Slf4j
@Service
public class AdminRpcService {
    @DubboReference(version = "1.0.0", check = false)
    private TokenAuthenticationDubboService tokenAuthenticationDubboService;

    /**
     * 根据token调用admin服务rpc接口查询admin用户认证信息
     * @param token
     * @return
     */
    public AdminUserAuthenticationBO getAdminUserAuthenticationByToken(String token) {
        try {
            RpcResult<AdminUserAuthenticationDTO> result = tokenAuthenticationDubboService.findUserAuthenticationByToken(token);
            if (result.isFailed() || Objects.isNull(result.getData()) || Objects.isNull(result.getData().getAccessTokenDTO())) {
                log.error("AdminRpcService[getAdminUserAuthenticationByToken] 获取admin用户认证信息失败 token is {}, result is {}, errorMsg:{}"
                        , token
                        , result
                        , result.getMessage());
                throw new BizException(GatewayRetCodeEnum.GATEWAY_USER_AUTHENTICATION_FAILED);
            }
            return AdminUserAuthenticationHelper.convertBO(result.getData());
        } catch (Exception e) {
            log.error("AdminRpcService[getAdminUserAuthenticationByToken] 获取admin用户认证信息失败, token is {}，caused by {}"
                    , token
                    , Throwables.getStackTraceAsString(e));
            throw new BizException(GatewayRetCodeEnum.GATEWAY_USER_AUTHENTICATION_FAILED);
        }
    }
}
