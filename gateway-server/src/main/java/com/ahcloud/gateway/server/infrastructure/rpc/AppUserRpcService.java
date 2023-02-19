package com.ahcloud.gateway.server.infrastructure.rpc;

import com.ahcloud.admin.client.domain.dubbo.token.AdminUserAuthenticationDTO;
import com.ahcloud.admin.dubbo.authentication.TokenAuthenticationDubboService;
import com.ahcloud.common.result.RpcResult;
import com.ahcloud.gateway.client.enums.GatewayRetCodeEnum;
import com.ahcloud.gateway.server.application.helper.AdminUserAuthenticationHelper;
import com.ahcloud.gateway.server.application.helper.AppUserAuthenticationHelper;
import com.ahcloud.gateway.server.domain.admin.bo.AdminUserAuthenticationBO;
import com.ahcloud.gateway.server.domain.app.AppUserAuthenticationBO;
import com.ahcloud.gateway.server.infrastructure.exception.BizException;
import com.ahcloud.gateway.server.infrastructure.exception.TokenExpiredException;
import com.ahcloud.uaa.client.domain.dubbo.token.AppUserAuthenticationDTO;
import com.ahcloud.uaa.dubbo.authentication.AppTokenAuthenticationDubboService;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.checkerframework.checker.units.qual.A;
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
public class AppUserRpcService {
    @DubboReference(version = "1.0.0", check = false)
    private AppTokenAuthenticationDubboService tokenAuthenticationDubboService;

    /**
     * 根据token调用admin服务rpc接口查询admin用户认证信息
     * @param token
     * @return
     */
    public AppUserAuthenticationBO getAppUserAuthenticationByToken(String token) {
        try {
            RpcResult<AppUserAuthenticationDTO> result = tokenAuthenticationDubboService.findUserAuthenticationByToken(token);
            AppUserAuthenticationDTO userAuthenticationDTO = result.getData();
            if (result.isFailed() || Objects.isNull(userAuthenticationDTO) || Objects.isNull(userAuthenticationDTO.getAccessTokenDTO())) {
                log.error("AppUserRpcService[getAppUserAuthenticationByToken] 获取app用户认证信息失败 token is {}, result is {}, errorMsg:{}"
                        , token
                        , result
                        , result.getMessage());
                throw new BizException(GatewayRetCodeEnum.GATEWAY_USER_AUTHENTICATION_FAILED);
            }
            // 判断token是否过期
            Boolean tokenExpired = userAuthenticationDTO.getTokenExpired();
            if (tokenExpired) {
                throw new TokenExpiredException();
            }
            return AppUserAuthenticationHelper.convertBO(userAuthenticationDTO);
        } catch (Exception e) {
            log.error("AppUserRpcService[getAppUserAuthenticationByToken] 获取app用户认证信息失败, token is {}，caused by {}"
                    , token
                    , Throwables.getStackTraceAsString(e));
            throw new BizException(GatewayRetCodeEnum.GATEWAY_USER_AUTHENTICATION_FAILED);
        }
    }
}
