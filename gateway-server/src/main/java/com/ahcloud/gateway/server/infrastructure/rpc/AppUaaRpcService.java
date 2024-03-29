//package com.ahcloud.gateway.server.infrastructure.rpc;
//
//import com.ahcloud.common.result.RpcResult;
//import com.ahcloud.gateway.client.enums.GatewayRetCodeEnum;
//import com.ahcloud.gateway.client.exception.BizException;
//import com.ahcloud.gateway.client.exception.TokenExpiredException;
//import com.ahcloud.gateway.server.helper.AppUserAuthenticationHelper;
//import com.ahcloud.gateway.server.domain.app.AppUserAuthenticationBO;
//import com.google.common.base.Throwables;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.dubbo.config.annotation.DubboReference;
//import org.springframework.stereotype.Service;
//
//import java.util.Objects;
//
///**
// * @program: ahcloud-gateway
// * @description: app uaa rpc服务
// * @author: YuKai Fan
// * @create: 2023/1/31 16:28
// **/
//@Slf4j
//@Service
//public class AppUaaRpcService {
//    @DubboReference(version = "1.0.0", check = false)
//    private AppTokenAuthenticationDubboService tokenAuthenticationDubboService;
//
//    /**
//     * 根据token调用app服务rpc接口查询app用户认证信息
//     * @param token
//     * @return
//     */
//    public AppUserAuthenticationBO getAppUserAuthenticationByToken(String token) {
//        try {
//            RpcResult<AppUserAuthenticationDTO> result = tokenAuthenticationDubboService.findUserAuthenticationByToken(token);
//            AppUserAuthenticationDTO userAuthenticationDTO = result.getData();
//            if (result.isFailed() || Objects.isNull(userAuthenticationDTO) || Objects.isNull(userAuthenticationDTO.getAccessTokenDTO())) {
//                log.error("AppUaaRpcService[getAppUserAuthenticationByToken] 获取app用户认证信息失败 token is {}, result is {}, errorMsg:{}"
//                        , token
//                        , result
//                        , result.getMessage());
//                throw new BizException(GatewayRetCodeEnum.GATEWAY_USER_AUTHENTICATION_FAILED);
//            }
//            // 判断token是否过期
//            Boolean tokenExpired = userAuthenticationDTO.getTokenExpired();
//            if (tokenExpired) {
//                throw new TokenExpiredException();
//            }
//            return AppUserAuthenticationHelper.convertBO(userAuthenticationDTO);
//        } catch (Exception e) {
//            log.error("AppUaaRpcService[getAppUserAuthenticationByToken] 获取app用户认证信息失败, token is {}，caused by {}"
//                    , token
//                    , Throwables.getStackTraceAsString(e));
//            throw new BizException(GatewayRetCodeEnum.GATEWAY_USER_AUTHENTICATION_FAILED);
//        }
//    }
//}
