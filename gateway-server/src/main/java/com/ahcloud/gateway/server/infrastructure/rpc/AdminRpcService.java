package com.ahcloud.gateway.server.infrastructure.rpc;

import com.ahcloud.admin.client.domain.dubbo.base.BaseAuthorityDTO;
import com.ahcloud.admin.client.domain.dubbo.base.query.BaseAuthorityPageQueryDTO;
import com.ahcloud.admin.client.domain.dubbo.token.AdminUserAuthenticationDTO;
import com.ahcloud.admin.client.enums.ReadOrWriteEnum;
import com.ahcloud.admin.dubbo.authentication.TokenAuthenticationDubboService;
import com.ahcloud.admin.dubbo.base.AdminBaseDubboService;
import com.ahcloud.common.result.RpcResult;
import com.ahcloud.common.result.page.RpcPageResult;
import com.ahcloud.common.utils.CollectionUtils;
import com.ahcloud.gateway.client.enums.GatewayRetCodeEnum;
import com.ahcloud.gateway.server.application.helper.AdminUserAuthenticationHelper;
import com.ahcloud.gateway.server.domain.admin.bo.AdminAuthorityBO;
import com.ahcloud.gateway.server.domain.admin.bo.AdminUserAuthenticationBO;
import com.ahcloud.gateway.server.domain.admin.query.AdminAuthorityQuery;
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

    /**
     * 权限查询默认分页大小, 不得超过100
     */
    private final static int DEFAULT_BATCH_SIZE = 100;

    @DubboReference(version = "1.0.0")
    private AdminBaseDubboService adminBaseDubboService;
    @DubboReference(version = "1.0.0")
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
                throw new GatewayException(GatewayRetCodeEnum.GATEWAY_USER_AUTHENTICATION_FAILED);
            }
            return AdminUserAuthenticationHelper.convertBO(result.getData());
        } catch (Exception e) {
            log.error("AdminRpcService[getAdminUserAuthenticationByToken] 试算退货金额失败, token is {}，caused by {}"
                    , token
                    , Throwables.getStackTraceAsString(e));
            throw new GatewayException(GatewayRetCodeEnum.GATEWAY_USER_AUTHENTICATION_FAILED);
        }
    }

    /**
     * 获取权限列表
     * @param query
     * @return
     */
    public List<AdminAuthorityBO> listAdminAuthorities(AdminAuthorityQuery query) {
        BaseAuthorityPageQueryDTO queryDTO = BaseAuthorityPageQueryDTO.builder()
                .authority(query.getAuthority())
                .name(query.getName())
                .path(query.getPath())
                .readOrWriteEnum(ReadOrWriteEnum.getByType(query.getReadOrWriteEnum().getType()))
                .build();
        queryDTO.setPageNo(query.getPageNo());
        // 权限列表最大查询100条
        queryDTO.setPageSize(query.getPageSize() > DEFAULT_BATCH_SIZE ? DEFAULT_BATCH_SIZE : query.getPageSize());
        try {
            RpcPageResult<BaseAuthorityDTO> result = adminBaseDubboService.pageBaseAuthorities(queryDTO);
            if (result.isFailed()) {
                log.error("AdminRpcService[listAdminAuthorities] 获取admin 权限列表失败 queryRequest is {}, result is {}, errorMsg:{}"
                        , query
                        , result
                        , result.getMessage());
                throw new GatewayException(GatewayRetCodeEnum.AUTHORIZATION_GET_ERROR);
            }
            return this.convertToBo(result.getRows());
        } catch (Exception e) {
            log.error("AdminRpcService[listAdminAuthorities] 获取admin 权限列表失败 queryRequest is {}，caused by {}"
                    , query
                    , Throwables.getStackTraceAsString(e));
            throw new GatewayException(GatewayRetCodeEnum.AUTHORIZATION_GET_ERROR);
        }
    }

    private List<AdminAuthorityBO> convertToBo(List<BaseAuthorityDTO> baseAuthorityList) {
        return baseAuthorityList.stream()
                .map(baseAuthorityDTO ->
                        AdminAuthorityBO.builder()
                                .serviceId(baseAuthorityDTO.getServiceId())
                                .auth(baseAuthorityDTO.getAuth())
                                .enabled(baseAuthorityDTO.getEnabled())
                                .path(baseAuthorityDTO.getPath())
                                .authority(baseAuthorityDTO.getAuthority())
                                .build()
                )
                .collect(Collectors.toList());
    }
}
