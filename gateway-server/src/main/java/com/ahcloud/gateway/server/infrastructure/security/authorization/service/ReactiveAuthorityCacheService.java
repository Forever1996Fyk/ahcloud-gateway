package com.ahcloud.gateway.server.infrastructure.security.authorization.service;

import com.ahcloud.gateway.server.domain.admin.bo.AdminAuthorityBO;
import org.springframework.beans.factory.InitializingBean;

import java.util.List;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/2/1 17:11
 **/
public interface ReactiveAuthorityCacheService extends InitializingBean {

    /**
     * 初始化缓存
     */
    void initCache();

    /**
     * 更新缓存
     * @param authorityBO
     */
    void updateCache(AdminAuthorityBO authorityBO);

    /**
     * 从缓存中获取权限列表
     *
     * @return
     */
    List<AdminAuthorityBO> listAdminAuthoritiesFromCache();

    /**
     * 从缓存中根据 path获取权限信息
     * @param path
     * @return
     */
    AdminAuthorityBO getAdminAuthorityFromCacheByPath(String path);
}
