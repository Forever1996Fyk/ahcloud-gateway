package com.ahcloud.gateway.server.infrastructure.rpc;

import com.ahcloud.admin.client.domain.dubbo.token.AdminUserAuthenticationDTO;
import com.ahcloud.admin.dubbo.authentication.TokenAuthenticationDubboService;
import com.ahcloud.common.result.RpcResult;
import com.ahcloud.gateway.client.enums.GatewayRetCodeEnum;
import com.ahcloud.gateway.core.infrastructure.exception.BizException;
import com.ahcloud.gateway.core.infrastructure.exception.TokenExpiredException;
import com.ahcloud.gateway.server.helper.AdminUserAuthenticationHelper;
import com.ahcloud.gateway.server.domain.admin.bo.AdminUserAuthenticationBO;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import java.util.Objects;

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
            AdminUserAuthenticationDTO adminUserAuthenticationDTO = result.getData();
            if (result.isFailed() || Objects.isNull(adminUserAuthenticationDTO) || Objects.isNull(adminUserAuthenticationDTO.getAccessTokenDTO())) {
                log.error("AdminRpcService[getAdminUserAuthenticationByToken] 获取admin用户认证信息失败 token is {}, result is {}, errorMsg:{}"
                        , token
                        , result
                        , result.getMessage());
                throw new BizException(GatewayRetCodeEnum.GATEWAY_USER_AUTHENTICATION_FAILED);
            }
            // 判断token是否过期
            Boolean tokenExpired = adminUserAuthenticationDTO.getTokenExpired();
            if (tokenExpired) {
                throw new TokenExpiredException();
            }
            return AdminUserAuthenticationHelper.convertBO(adminUserAuthenticationDTO);
        } catch (TokenExpiredException e) {
            throw e;
        } catch (Exception e) {
            log.error("AdminRpcService[getAdminUserAuthenticationByToken] 获取admin用户认证信息失败, token is {}，caused by {}"
                    , token
                    , Throwables.getStackTraceAsString(e));
            throw new BizException(GatewayRetCodeEnum.GATEWAY_USER_AUTHENTICATION_FAILED);
        }
    }
}
