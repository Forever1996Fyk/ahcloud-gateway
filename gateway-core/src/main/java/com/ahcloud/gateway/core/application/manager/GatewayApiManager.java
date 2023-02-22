package com.ahcloud.gateway.core.application.manager;

import com.ahcloud.common.result.page.PageResult;
import com.ahcloud.gateway.client.common.DeletedEnum;
import com.ahcloud.gateway.client.enums.ApiStatusEnum;
import com.ahcloud.gateway.client.enums.GatewayRetCodeEnum;
import com.ahcloud.gateway.core.application.helper.GatewayApiHelper;
import com.ahcloud.gateway.core.application.service.GatewayApiService;
import com.ahcloud.gateway.core.domain.api.dto.ApiRefreshDTO;
import com.ahcloud.gateway.core.domain.api.form.ApiAddForm;
import com.ahcloud.gateway.core.domain.api.form.ApiUpdateForm;
import com.ahcloud.gateway.core.domain.api.query.ApiQuery;
import com.ahcloud.gateway.core.domain.api.vo.ApiVO;
import com.ahcloud.gateway.core.infrastructure.exception.BizException;
import com.ahcloud.gateway.core.infrastructure.gateway.enums.ConfigGroupEnum;
import com.ahcloud.gateway.core.infrastructure.gateway.enums.DataEventTypeEnum;
import com.ahcloud.gateway.core.infrastructure.gateway.listener.event.DataChangedEvent;
import com.ahcloud.gateway.core.infrastructure.repository.bean.GatewayApi;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
            throw new BizException(GatewayRetCodeEnum.GATEWAY_API_CODE_EXITED);
        }
        GatewayApi gatewayApi = GatewayApiHelper.convert(form);
        boolean result = gatewayApiService.save(gatewayApi);
        if (!result) {
            throw new BizException(GatewayRetCodeEnum.GATEWAY_API_ADD_FAILED);
        }
        applicationEventPublisher.publishEvent(
                new DataChangedEvent(GatewayApiHelper.convertToDTO(gatewayApi), DataEventTypeEnum.CREATE, ConfigGroupEnum.API)
        );
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
            throw new BizException(GatewayRetCodeEnum.GATEWAY_API_NOT_EXITED);
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
            throw new BizException(GatewayRetCodeEnum.GATEWAY_API_UPDATE_FAILED);
        }
        applicationEventPublisher.publishEvent(
                new DataChangedEvent(GatewayApiHelper.convertToDTO(gatewayApi), DataEventTypeEnum.UPDATE, ConfigGroupEnum.API)
        );
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
            throw new BizException(GatewayRetCodeEnum.GATEWAY_API_NOT_EXITED);
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
            throw new BizException(GatewayRetCodeEnum.GATEWAY_API_DELETE_FAILED);
        }
        applicationEventPublisher.publishEvent(
                new DataChangedEvent(GatewayApiHelper.convertToDTO(gatewayApi), DataEventTypeEnum.DELETE, ConfigGroupEnum.API)
        );
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
            throw new BizException(GatewayRetCodeEnum.GATEWAY_API_NOT_EXITED);
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

    /**
     * 刷新接口
     */
    public void refreshApi() {
        List<GatewayApi> gatewayApiList = gatewayApiService.list(
                new QueryWrapper<GatewayApi>().lambda()
                        .eq(GatewayApi::getDeleted, DeletedEnum.NO.value)
        );
        applicationEventPublisher.publishEvent(
                new DataChangedEvent(GatewayApiHelper.convertToDTOList(gatewayApiList), DataEventTypeEnum.REFRESH, ConfigGroupEnum.API)
        );
    }

    /**
     * 下线接口
     * @param id
     */
    public void offlineApi(Long id) {
        operateApi(id, ApiStatusEnum.OFFLINE);
    }

    /**
     * 下线接口
     * @param id
     */
    public void onlineApi(Long id) {
        operateApi(id, ApiStatusEnum.NORMAL);
    }

    private void operateApi(Long id, ApiStatusEnum apiStatusEnum) {
        GatewayApi existedGatewayApi = gatewayApiService.getOne(
                new QueryWrapper<GatewayApi>().lambda()
                        .eq(GatewayApi::getId, id)
                        .eq(GatewayApi::getDeleted, DeletedEnum.NO.value)
        );
        if (Objects.isNull(existedGatewayApi)) {
            throw new BizException(GatewayRetCodeEnum.GATEWAY_API_NOT_EXITED);
        }
        GatewayApi updateGatewayApi = new GatewayApi();
        updateGatewayApi.setStatus(apiStatusEnum.getStatus());
        updateGatewayApi.setVersion(existedGatewayApi.getVersion());
        boolean updateResult = gatewayApiService.update(
                updateGatewayApi,
                new UpdateWrapper<GatewayApi>().lambda()
                        .eq(GatewayApi::getId, id)
                        .eq(GatewayApi::getVersion, existedGatewayApi.getVersion())
        );
        if (!updateResult) {
            throw new BizException(GatewayRetCodeEnum.VERSION_ERROR);
        }

        existedGatewayApi.setStatus(apiStatusEnum.getStatus());
        ApiRefreshDTO apiRefreshDTO = GatewayApiHelper.convertToDTO(existedGatewayApi);
        applicationEventPublisher.publishEvent(
                new DataChangedEvent(apiRefreshDTO, DataEventTypeEnum.UPDATE, ConfigGroupEnum.API)
        );
    }
}
