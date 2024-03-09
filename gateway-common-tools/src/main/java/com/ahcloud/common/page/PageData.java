package com.ahcloud.common.page;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
* @program: ahcloud-common
* @description:
* @author: YuKai Fan
* @create: 2023/5/12 11:10
**/
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PageData implements Serializable {
    private static final long serialVersionUID = -7279838160318728094L;

    /**
     * 当前页数
     */
    private int pageNum;

    /**
     * 每页数量
     */
    private int pageSize;

    /**
     * 查询总数
     */
    private long total;

    /**
     * 总页数
     */
    private int pages;

    public int getPages() {
        if (this.pages == 0) {
            return (int) (total / pageSize + ((total % pageSize == 0) ? 0 : 1));
        } else {
            return this.pages;
        }
    }

}
