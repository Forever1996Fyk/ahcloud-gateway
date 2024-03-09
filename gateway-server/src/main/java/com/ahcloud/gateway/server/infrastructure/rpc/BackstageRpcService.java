//package com.ahcloud.gateway.server.infrastructure.rpc;
//
//import com.ahcloud.admin.client.domain.dubbo.token.response.AdminUserAuthenticationResponse;
//import com.ahcloud.admin.dubbo.authentication.TokenAuthenticationDubboService;
//import com.ahcloud.common.result.RpcResult;
//import com.ahcloud.gateway.client.enums.GatewayRetCodeEnum;
//import com.ahcloud.gateway.client.exception.BizException;
//import com.ahcloud.gateway.client.exception.TokenExpiredException;
//import com.ahcloud.gateway.server.helper.BackstageUserAuthenticationHelper;
//import com.ahcloud.gateway.server.domain.admin.bo.BackstageUserAuthenticationBO;
//import com.google.common.base.Throwables;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.dubbo.config.annotation.DubboReference;
//import org.springframework.stereotype.Service;
//
//import java.util.Objects;
//
///**
// * @program: ahcloud-gateway
// * @description: 后台 rpc服务
// * @author: YuKai Fan
// * @create: 2023/1/31 16:28
// **/
//@Slf4j
//@Service
//public class BackstageRpcService {
//    @DubboReference(version = "1.0.0", check = false, stub = "true")
//    private TokenAuthenticationDubboService tokenAuthenticationDubboService;
//
//    /**
//     * 根据token调用admin服务rpc接口查询admin用户认证信息
//     * @param token
//     * @return
//     */
//    public BackstageUserAuthenticationBO getAdminUserAuthenticationByToken(String token) {
//        try {
//            RpcResult<AdminUserAuthenticationResponse> result = tokenAuthenticationDubboService.findUserAuthenticationByToken(token);
//            AdminUserAuthenticationResponse response = result.getData();
//            if (result.isFailed() || Objects.isNull(response) || Objects.isNull(response.getAccessTokenDTO())) {
//                log.error("BackstageRpcService[getAdminUserAuthenticationByToken] 获取admin用户认证信息失败 token is {}, result is {}, errorMsg:{}"
//                        , token
//                        , result
//                        , result.getMessage());
//                throw new BizException(GatewayRetCodeEnum.GATEWAY_USER_AUTHENTICATION_FAILED);
//            }
//            // 判断token是否过期
//            Boolean tokenExpired = response.getTokenExpired();
//            if (tokenExpired) {
//                throw new TokenExpiredException();
//            }
//            return BackstageUserAuthenticationHelper.convertBO(response);
//        } catch (TokenExpiredException e) {
//            throw e;
//        } catch (Exception e) {
//            log.error("BackstageRpcService[getAdminUserAuthenticationByToken] 获取admin用户认证信息失败, token is {}，caused by {}"
//                    , token
//                    , Throwables.getStackTraceAsString(e));
//            throw new BizException(GatewayRetCodeEnum.GATEWAY_USER_AUTHENTICATION_FAILED);
//        }
//    }
//}
