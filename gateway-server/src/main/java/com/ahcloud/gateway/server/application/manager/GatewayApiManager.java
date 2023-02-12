package com.ahcloud.gateway.server.application.manager;

import com.ahcloud.common.result.page.PageResult;
import com.ahcloud.gateway.client.common.DeletedEnum;
import com.ahcloud.gateway.client.enums.ApiStatusEnum;
import com.ahcloud.gateway.client.enums.GatewayRetCodeEnum;
import com.ahcloud.gateway.server.application.helper.GatewayApiHelper;
import com.ahcloud.gateway.server.application.helper.RouteHelper;
import com.ahcloud.gateway.server.application.service.GatewayApiService;
import com.ahcloud.gateway.server.domain.api.form.ApiAddForm;
import com.ahcloud.gateway.server.domain.api.form.ApiUpdateForm;
import com.ahcloud.gateway.server.domain.api.query.ApiQuery;
import com.ahcloud.gateway.server.domain.api.vo.ApiVO;
import com.ahcloud.gateway.server.infrastructure.exception.GatewayException;
import com.ahcloud.gateway.server.infrastructure.gateway.enums.ConfigGroupEnum;
import com.ahcloud.gateway.server.infrastructure.gateway.enums.DataEventTypeEnum;
import com.ahcloud.gateway.server.infrastructure.gateway.listener.event.DataChangedEvent;
import com.ahcloud.gateway.server.infrastructure.gateway.service.GatewayService;
import com.ahcloud.gateway.server.infrastructure.repository.bean.GatewayApi;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/2/4 10:36
 **/
@Slf4j
@Component
public class GatewayApiManager {
    @Resource
    private GatewayService gatewayService;
    @Resource
    private GatewayApiService gatewayApiService;
    @Resource
    private ApplicationEventPublisher applicationEventPublisher;

    /**
     * 新增api接口
     * @param form
     */
    @Transactional(rollbackFor = Exception.class)
    public void createApi(ApiAddForm form) {
        GatewayApi existedGatewayApi = gatewayApiService.getOne(
                new QueryWrapper<GatewayApi>().lambda()
                        .eq(GatewayApi::getApiCode, form.getApiCode())
                        .eq(GatewayApi::getDeleted, DeletedEnum.NO.value)
        );
        if (Objects.nonNull(existedGatewayApi)) {
            throw new GatewayException(GatewayRetCodeEnum.GATEWAY_API_CODE_EXITED);
        }
        GatewayApi gatewayApi = GatewayApiHelper.convert(form);
        boolean result = gatewayApiService.save(gatewayApi);
        if (!result) {
            throw new GatewayException(GatewayRetCodeEnum.GATEWAY_API_ADD_FAILED);
        }
    }

    /**
     * 更新api接口
     * @param form
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateApi(ApiUpdateForm form) {
        GatewayApi existedGatewayApi = gatewayApiService.getOne(
                new QueryWrapper<GatewayApi>().lambda()
                        .eq(GatewayApi::getId, form.getId())
                        .eq(GatewayApi::getDeleted, DeletedEnum.NO.value)
        );
        if (Objects.isNull(existedGatewayApi)) {
            throw new GatewayException(GatewayRetCodeEnum.GATEWAY_API_NOT_EXITED);
        }
        GatewayApi gatewayApi = GatewayApiHelper.convert(form);
        gatewayApi.setVersion(existedGatewayApi.getVersion());
        boolean result = gatewayApiService.update(
                gatewayApi,
                new UpdateWrapper<GatewayApi>().lambda()
                        .eq(GatewayApi::getId, form.getId())
                        .eq(GatewayApi::getVersion, existedGatewayApi.getVersion())
        );
        if (!result) {
            throw new GatewayException(GatewayRetCodeEnum.GATEWAY_API_UPDATE_FAILED);
        }
    }

    /**
     * 删除api接口
     * @param id
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteApi(Long id) {
        GatewayApi existedGatewayApi = gatewayApiService.getOne(
                new QueryWrapper<GatewayApi>().lambda()
                        .eq(GatewayApi::getId, id)
                        .eq(GatewayApi::getDeleted, DeletedEnum.NO.value)
        );
        if (Objects.isNull(existedGatewayApi)) {
            throw new GatewayException(GatewayRetCodeEnum.GATEWAY_API_NOT_EXITED);
        }
        GatewayApi gatewayApi = new GatewayApi();
        gatewayApi.setDeleted(id);
        gatewayApi.setVersion(existedGatewayApi.getVersion());
        boolean result = gatewayApiService.update(
                gatewayApi,
                new UpdateWrapper<GatewayApi>().lambda()
                        .eq(GatewayApi::getId, id)
                        .eq(GatewayApi::getVersion, existedGatewayApi.getVersion())
        );
        if (!result) {
            throw new GatewayException(GatewayRetCodeEnum.GATEWAY_API_DELETE_FAILED);
        }
        gatewayService.existedRoute(gatewayApi.getServiceId())
                .map(item -> {
                    if (item) {
                        applicationEventPublisher.publishEvent(
                                new DataChangedEvent(RouteHelper.buildRoute(gatewayApi), DataEventTypeEnum.DELETE, ConfigGroupEnum.ROUTE)
                        );
                    }
                    return Mono.empty();
                }).subscribe();
    }

    /**
     * 根据id查询api信息
     *
     * @param id
     * @return
     */
    public ApiVO findApiById(Long id) {
        GatewayApi existedGatewayApi = gatewayApiService.getOne(
                new QueryWrapper<GatewayApi>().lambda()
                        .eq(GatewayApi::getId, id)
                        .eq(GatewayApi::getDeleted, DeletedEnum.NO.value)
        );
        if (Objects.isNull(existedGatewayApi)) {
            throw new GatewayException(GatewayRetCodeEnum.GATEWAY_API_NOT_EXITED);
        }
        return GatewayApiHelper.convertToVo(existedGatewayApi);
    }

    /**
     * 分页查询api接口
     * @param query
     * @return
     */
    public PageResult<ApiVO> pageApiList(ApiQuery query) {
        PageInfo<GatewayApi> pageInfo = PageHelper.startPage(query.getPageNo(), query.getPageSize())
                .doSelectPageInfo(
                        () -> gatewayApiService.list(
                                new QueryWrapper<GatewayApi>().lambda()
                                        .eq(
                                                StringUtils.isNotBlank(query.getApiCode()),
                                                GatewayApi::getApiCode,
                                                query.getApiCode()
                                        )
                                        .like(
                                                StringUtils.isNotBlank(query.getName()),
                                                GatewayApi::getApiName,
                                                query.getName()
                                        )
                                        .eq(
                                                StringUtils.isNotBlank(query.getServiceId()),
                                                GatewayApi::getServiceId,
                                                query.getServiceId()
                                        )
                        )
                );
        return GatewayApiHelper.convertToPageResult(pageInfo);
    }
}
