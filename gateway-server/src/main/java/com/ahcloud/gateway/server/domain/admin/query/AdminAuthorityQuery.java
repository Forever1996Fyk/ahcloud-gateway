package com.ahcloud.gateway.server.domain.admin.query;

import com.ahcloud.common.page.PageQuery;
import lombok.Data;

import java.io.Serializable;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/2/1 16:50
 **/
@Data
public class AdminAuthorityQuery implements Serializable {

    private static final long serialVersionUID = -4841562523002404406L;
    /**
     * 权限标识
     */
    private String authority;

    /**
     * 路径
     */
    private String path;

    /**
     * 权限名称
     */
    private String name;
}
