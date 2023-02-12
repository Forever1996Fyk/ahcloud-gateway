package com.ahcloud.gateway.server.application.helper;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.PageUtil;
import com.ahcloud.common.result.page.PageResult;
import com.ahcloud.gateway.client.enums.ApiStatusEnum;
import com.ahcloud.gateway.server.domain.api.form.ApiAddForm;
import com.ahcloud.gateway.server.domain.api.form.ApiUpdateForm;
import com.ahcloud.gateway.server.domain.api.vo.ApiVO;
import com.ahcloud.gateway.server.infrastructure.repository.bean.GatewayApi;
import com.github.pagehelper.PageInfo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/2/4 11:03
 **/
public class GatewayApiHelper {

    /**
     * 数据转换
     * @param form
     * @return
     */
    public static GatewayApi convert(ApiAddForm form) {
        GatewayApi gatewayApi = Convert.INSTANCE.convert(form);
        gatewayApi.setStatus(ApiStatusEnum.NORMAL.getStatus());
        gatewayApi.setCreator("system");
        gatewayApi.setModifier("system");
        return gatewayApi;
    }

    /**
     * 数据转换
     * @param form
     * @return
     */
    public static GatewayApi convert(ApiUpdateForm form) {
        return Convert.INSTANCE.convert(form);
    }

    /**
     * 数据转换
     * @param gatewayApi
     * @return
     */
    public static ApiVO convertToVo(GatewayApi gatewayApi) {
        ApiVO apiVO = Convert.INSTANCE.convertToVo(gatewayApi);
        apiVO.setClassName(
                gatewayApi.getQualifiedName() + "." + gatewayApi.getMethodName()
        );
        return apiVO;
    }

    /**
     * 数据转换
     * @param pageInfo
     * @return
     */
    public static PageResult<ApiVO> convertToPageResult(PageInfo<GatewayApi> pageInfo) {
        List<GatewayApi> list = pageInfo.getList();
        List<ApiVO> apiVOList = Convert.INSTANCE.convertToVoList(list);
        PageResult<ApiVO> pageResult = new PageResult<>();
        pageResult.setPageSize(pageInfo.getPageSize());
        pageResult.setPageNum(pageInfo.getPageNum());
        pageResult.setPages(pageInfo.getPages());
        pageResult.setRows(apiVOList);
        pageResult.setTotal(pageInfo.getTotal());
        return pageResult;
    }

    @Mapper
    public interface Convert {
        GatewayApiHelper.Convert INSTANCE = Mappers.getMapper(GatewayApiHelper.Convert.class);

        /**
         * 数据转换
         * @param form
         * @return
         */
        GatewayApi convert(ApiAddForm form);


        /**
         * 数据转换
         * @param form
         * @return
         */
        GatewayApi convert(ApiUpdateForm form);

        /**
         * 数据转换
         * @param gatewayApi
         * @return
         */
        ApiVO convertToVo(GatewayApi gatewayApi);

        /**
         * 数据转换
         * @param gatewayApiList
         * @return
         */
        List<ApiVO> convertToVoList(List<GatewayApi> gatewayApiList);
    }
}
