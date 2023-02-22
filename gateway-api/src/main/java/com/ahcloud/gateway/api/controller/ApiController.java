package com.ahcloud.gateway.api.controller;

import com.ahcloud.common.result.ResponseResult;
import com.ahcloud.common.result.page.PageResult;
import com.ahcloud.gateway.core.application.manager.GatewayApiManager;
import com.ahcloud.gateway.core.domain.api.form.ApiAddForm;
import com.ahcloud.gateway.core.domain.api.form.ApiUpdateForm;
import com.ahcloud.gateway.core.domain.api.query.ApiQuery;
import com.ahcloud.gateway.core.domain.api.vo.ApiVO;
import com.ahcloud.gateway.core.domain.response.GatewayResponseResult;
import com.ahcloud.gateway.starter.annotation.GatewaySpringCloudClient;
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
@GatewaySpringCloudClient
public class ApiController {
    @Resource
    private GatewayApiManager gatewayApiManager;

    /**
     * 添加api接口
     * @param form
     * @return
     */
    @PostMapping("/add")
    @GatewaySpringCloudClient
    public ResponseResult<Void> addApi(@RequestBody @Valid ApiAddForm form) {
        gatewayApiManager.createApi(form);
        return ResponseResult.ofSuccess();
    }

    /**
     * 更新api接口
     * @param form
     * @return
     */
    @PostMapping("/update")
    @GatewaySpringCloudClient
    public ResponseResult<Void> updateApi(@RequestBody @Valid ApiUpdateForm form) {
        gatewayApiManager.updateApi(form);
        return ResponseResult.ofSuccess();
    }

    /**
     * 删除api接口
     * @param id
     * @return
     */
    @PostMapping("/deleteById/{id}")
    @GatewaySpringCloudClient
    public ResponseResult<Void> deleteApiById(@PathVariable("id") Long id) {
        gatewayApiManager.deleteApi(id);
        return ResponseResult.ofSuccess();
    }

    /**
     * 根据id获取api详情
     * @param id
     * @return
     */
    @GetMapping("/findById/{id}")
    @GatewaySpringCloudClient
    public ResponseResult<ApiVO> findApiById(@PathVariable("id") Long id) {
        return ResponseResult.ofSuccess(gatewayApiManager.findApiById(id));
    }

    /**
     * 分页查询api列表
     * @param query
     * @return
     */
    @GetMapping("/page")
    @GatewaySpringCloudClient
    public ResponseResult<PageResult<ApiVO>> pageApiList(ApiQuery query) {
        return ResponseResult.ofSuccess(gatewayApiManager.pageApiList(query));
    }

    /**
     * 下线接口
     * @param id
     * @return
     */
    @PostMapping("/offlineApi/{id}")
    @GatewaySpringCloudClient
    public ResponseResult<Void> offlineApi(@PathVariable("id") Long id) {
        gatewayApiManager.offlineApi(id);
        return ResponseResult.ofSuccess();
    }

    /**
     * 上线接口
     * @param id
     * @return
     */
    @PostMapping("/onlineApi/{id}")
    @GatewaySpringCloudClient
    public ResponseResult<Void> onlineApi(@PathVariable("id") Long id) {
        gatewayApiManager.onlineApi(id);
        return ResponseResult.ofSuccess();
    }
}
