package com.ahcloud.gateway.server.infrastructure.security.authorization.authority.bo;

import com.ahcloud.gateway.client.enums.AuthorityReadOrWriteEnum;
import com.ahcloud.gateway.server.infrastructure.security.authorization.authority.UserReactiveAuthority;
import lombok.*;

import java.io.Serializable;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/2/1 14:50
 **/
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BackstageUserReactiveAuthorityBo implements Serializable, UserReactiveAuthority {
    private static final long serialVersionUID = -6293338339880004918L;
    /**
     * 用户id
     */
    private Long userId;

    /**
     * 租户id
     */
    private Long tenantId;

    /**
     * 权限标识
     */
    private String authority;

    /**
     * 请求路径
     */
    private String path;

    /**
     * 是否可用
     */
    private boolean enabled;

    /**
     * 是否认真
     */
    private boolean auth;

    /**
     * 读写类型
     */
    private AuthorityReadOrWriteEnum readOrWriteEnum;

}
