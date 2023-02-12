package com.ahcloud.gateway.server.api.controller;

import com.ahcloud.gateway.server.application.manager.GatewayApiManager;
import com.ahcloud.gateway.server.domain.api.form.ApiAddForm;
import com.ahcloud.gateway.server.domain.api.form.ApiUpdateForm;
import com.ahcloud.gateway.server.domain.api.query.ApiQuery;
import com.ahcloud.gateway.server.domain.response.GatewayResponseResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @program: ahcloud-gateway
 * @description:
 * @author: YuKai Fan
 * @create: 2023/2/3 23:12
 **/
@RestController
@RequestMapping("/api")
public class ApiController {
    @Resource
    private GatewayApiManager gatewayApiManager;

    /**
     * 添加api接口
     * @param form
     * @return
     */
    @PostMapping("/add")
    public GatewayResponseResult addApi(@RequestBody @Valid ApiAddForm form) {
        gatewayApiManager.createApi(form);
        return GatewayResponseResult.ofSuccess();
    }

    /**
     * 更新api接口
     * @param form
     * @return
     */
    @PostMapping("/update")
    public GatewayResponseResult updateApi(@RequestBody @Valid ApiUpdateForm form) {
        gatewayApiManager.updateApi(form);
        return GatewayResponseResult.ofSuccess();
    }

    /**
     * 删除api接口
     * @param id
     * @return
     */
    @PostMapping("/deleteById/{id}")
    public GatewayResponseResult deleteApiById(@PathVariable("id") Long id) {
        gatewayApiManager.deleteApi(id);
        return GatewayResponseResult.ofSuccess();
    }

    /**
     * 根据id获取api详情
     * @param id
     * @return
     */
    @GetMapping("/findApiById/{id}")
    public GatewayResponseResult findApiById(@PathVariable("id") Long id) {
        return GatewayResponseResult.ofSuccess(gatewayApiManager.findApiById(id));
    }

    /**
     * 分页查询api列表
     * @param query
     * @return
     */
    public GatewayResponseResult pageApiList(ApiQuery query) {
        return GatewayResponseResult.ofSuccess(gatewayApiManager.pageApiList(query));
    }
}
