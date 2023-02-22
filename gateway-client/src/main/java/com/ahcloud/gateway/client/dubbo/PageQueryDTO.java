package com.ahcloud.gateway.client.dubbo;

import lombok.Data;

import java.io.Serializable;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/2/20 17:19
 **/
@Data
public class PageQueryDTO implements Serializable {
    private static final long serialVersionUID = 8055144739090144663L;

    /**
     * 页数
     */
    private Integer pageNo;

    /**
     * 分页大小
     */
    private Integer pageSize;

    /**
     * 是否返回总数
     */
    private Boolean count;

    public Integer getPageNo() {
        return (this.pageNo == null || this.pageNo == 0) ? this.pageNo = 1 : this.pageNo;
    }

    public Integer getPageSize() {
        return (this.pageSize == null || this.pageSize == 0) ? this.pageSize = 10 : this.pageSize;
    }
}
