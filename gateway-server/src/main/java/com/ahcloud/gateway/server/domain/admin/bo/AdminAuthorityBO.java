package com.ahcloud.gateway.server.domain.admin.bo;

import com.ahcloud.gateway.client.enums.AuthorityReadOrWriteEnum;
import lombok.*;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/2/1 16:33
 **/
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AdminAuthorityBO {

    /**
     * 权限标识
     */
    private String authority;

    /**
     * 权限路径
     */
    private String path;

    /**
     * 服务id
     */
    private String serviceId;

    /**
     * 是否可用
     */
    private Boolean enabled;

    /**
     * 是否认证
     */
    private Boolean auth;

    /**
     * 读写类型
     */
    private AuthorityReadOrWriteEnum readOrWriteEnum;
}
