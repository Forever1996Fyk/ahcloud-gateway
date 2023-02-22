package com.ahcloud.gateway.test.webflux;

import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/2/20 20:10
 **/
public class PathTest {

    public static void main(String[] args) {
        PathPatternParser parser = new PathPatternParser();
        PathPattern pathPattern = parser.parse("/ahcloud-admin-api/sysMenu/deleteById/**");
        System.out.println(pathPattern.getPatternString());
    }
}
