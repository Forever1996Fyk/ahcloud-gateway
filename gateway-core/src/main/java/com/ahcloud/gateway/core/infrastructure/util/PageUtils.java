package com.ahcloud.gateway.core.infrastructure.util;

import com.ahcloud.common.result.page.PageResult;
import com.ahcloud.gateway.client.enums.GatewayRetCodeEnum;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @program: ahcloud-admin
 * @description:
 * @author: YuKai Fan
 * @create: 2023/2/2 11:20
 **/
public class PageUtils {

    /**
     * pageInfo 转为 pageResult
     * @param pageInfo
     * @param data
     * @return
     * @param <T>
     * @param <C>
     */
    public static  <T, C> PageResult<T> pageInfoConvertToPageResult(PageInfo<C> pageInfo, List<T> data) {
        PageResult<T> pageResult = new PageResult<>();
        pageResult.setPageNum(pageInfo.getPageNum());
        pageResult.setPages(pageInfo.getPages());
        pageResult.setPageSize(pageInfo.getPageSize());
        pageResult.setTotal(pageInfo.getTotal());
        pageResult.setRows(data);
        return pageResult;
    }
}
