package com.ahcloud.gateway.server.infrastructure;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/1/12 21:16
 **/
@RestController
@RequestMapping("/k8s")
public class TestController {

    @GetMapping("/test")
    public String test() {
        return "Success";
    }
}
