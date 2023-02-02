package com.ahcloud.gateway.server.infrastructure.security.authorization.service.impl;

import com.ahcloud.common.utils.CollectionUtils;
import com.ahcloud.gateway.server.domain.admin.bo.AdminAuthorityBO;
import com.ahcloud.gateway.server.domain.admin.query.AdminAuthorityQuery;
import com.ahcloud.gateway.server.infrastructure.rpc.AdminRpcService;
import com.ahcloud.gateway.server.infrastructure.security.authorization.service.ReactiveAuthorityCacheService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/2/2 08:53
 **/
@Slf4j
@Component
public class ReactiveAuthorityCacheServiceImpl implements ReactiveAuthorityCacheService {
    @Resource
    private AdminRpcService adminRpcService;

    private final static AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    private Map<String, AdminAuthorityBO> localAuthorityCache;

    @Override
    public void initCache() {
        loadAuthorities();
    }

    @Override
    public void updateCache(AdminAuthorityBO authorityBO) {

    }

    @Override
    public List<AdminAuthorityBO> listAdminAuthoritiesFromCache() {
        if (CollectionUtils.isEmpty(localAuthorityCache)) {
            loadAuthorities();
        }
        return Lists.newArrayList(localAuthorityCache.values());
    }

    @Override
    public AdminAuthorityBO getAdminAuthorityFromCacheByPath(String path) {
        if (CollectionUtils.isEmpty(localAuthorityCache)) {
            loadAuthorities();
        }
        for (Map.Entry<String, AdminAuthorityBO> entry : localAuthorityCache.entrySet()) {
            String key = entry.getKey();
            if (PATH_MATCHER.match(key, path)) {
                return entry.getValue();
            }
        }
        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        initCache();
        if (CollectionUtils.isEmpty(localAuthorityCache)) {
            throw new RuntimeException("网关启动初始化权限集合缓存失败");
        }
    }

    private void loadAuthorities() {
        int page = 1;
        long count = 0;
        AdminAuthorityQuery query = new AdminAuthorityQuery();
        query.setPageSize(100);
        while (true) {
            query.setPageNo(page);
            List<AdminAuthorityBO> authorityBOList = adminRpcService.listAdminAuthorities(query);
            if (CollectionUtils.isEmpty(authorityBOList)) {
                break;
            }
            Map<String, AdminAuthorityBO> authorityMap = CollectionUtils.convertMap(authorityBOList, AdminAuthorityBO::getPath);
            if (CollectionUtils.isEmpty(localAuthorityCache)) {
                localAuthorityCache = authorityMap;
            } else {
                localAuthorityCache.putAll(authorityMap);
            }
            count = count + authorityBOList.size();
            page++;
        }
        log.info("网关初始化加载权限集合缓存完成, totalPageNo is {}, total is {}", page, count);
    }
}
