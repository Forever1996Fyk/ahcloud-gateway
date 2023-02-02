package com.ahcloud.gateway.server.domain.admin.query;

import com.ahcloud.common.page.PageQuery;
import com.ahcloud.gateway.client.enums.AuthorityReadOrWriteEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/2/1 16:50
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AdminAuthorityQuery extends PageQuery implements Serializable {

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

    /**
     * 读写类型
     */
    private AuthorityReadOrWriteEnum readOrWriteEnum;
}
