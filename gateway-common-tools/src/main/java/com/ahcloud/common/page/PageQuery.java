package com.ahcloud.common.page;

import lombok.Data;

import java.util.Objects;

/**
 * @program: permissions-center
 * @description:
 * @author: YuKai Fan
 * @create: 2021-12-06 16:37
 **/
@Data
public class PageQuery {

    private final static int DEFAULT_PAGE_NUM = 1;
    private final static int DEFAULT_PAGE_SIZE = 10;

    /**
     * 当前页数
     */
    private Integer pageNo;

    /**
     * 每页数量
     */
    private Integer pageSize;

    public Integer getPageNo() {
        return Objects.isNull(pageNo) ? DEFAULT_PAGE_NUM : pageNo;
    }

    public Integer getPageSize() {
        return Objects.isNull(pageSize) ? DEFAULT_PAGE_SIZE : pageSize;
    }
}
