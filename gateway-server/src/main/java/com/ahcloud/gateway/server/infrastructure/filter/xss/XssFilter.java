package com.ahcloud.gateway.server.infrastructure.filter.xss;

import com.ahcloud.gateway.server.infrastructure.config.XssProperties;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @program: ahcloud-gateway
 * @description: Xss 过滤器
 * @author: YuKai Fan
 * @create: 2023/4/26 11:22
 **/
@Component
public class XssFilter extends OncePerRequestFilter implements Ordered {

    /**
     * 属性
     */
    private final XssProperties properties;

    /**
     * 路径匹配器
     */
    private final PathMatcher pathMatcher = new AntPathMatcher();

    public XssFilter(XssProperties properties) {
        this.properties = properties;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        filterChain.doFilter(new XssRequestWrapper(request), response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // 如果关闭，则不过滤
        if (!properties.isEnable()) {
            return true;
        }

        // 如果匹配到无需过滤，则不过滤
        String uri = request.getRequestURI();
        return properties.getExcludeUrls().stream().anyMatch(excludeUrl -> pathMatcher.match(excludeUrl, uri));
    }


    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE + 2;
    }
}
