package com.atguigu.daijia.mgr.controller;

import com.atguigu.daijia.common.result.Result;
import com.atguigu.daijia.customer.client.CustomerInfoFeignClient;
import com.atguigu.daijia.model.entity.customer.CustomerInfo;
import com.atguigu.daijia.model.vo.base.PageVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "用户管理API接口")
@RestController
@RequestMapping("/mgr/customer/info")
@SuppressWarnings({"unchecked", "rawtypes"})
public class CustomerInfoController {

    @Autowired
    private CustomerInfoFeignClient customerInfoFeignClient;

    @Operation(summary = "分页查询用户信息")
    @GetMapping("/findCustomerInfoPage/{page}/{limit}")
    public Result<PageVo<CustomerInfo>> findCustomerInfoPage(
            @Parameter(name = "page", description = "当前页码", required = true)
            @PathVariable Long page,
            @Parameter(name = "limit", description = "每页记录数", required = true)
            @PathVariable Long limit) {
        return customerInfoFeignClient.findCustomerInfoPage(page, limit);
    }

    @Operation(summary = "根据昵称分页查询用户信息")
    @GetMapping("/findCustomerInfoPageByName/{page}/{limit}")
    public Result<PageVo<CustomerInfo>> findCustomerInfoPageByName(
            @Parameter(name = "page", description = "当前页码", required = true)
            @PathVariable Long page,
            @Parameter(name = "limit", description = "每页记录数", required = true)
            @PathVariable Long limit,
            @Parameter(name = "nickname", description = "用户昵称", required = false)
            @RequestParam(required = false) String nickname) {
        return customerInfoFeignClient.findCustomerInfoPageByName(page, limit, nickname);
    }

    @Operation(summary = "更新用户状态")
    @PutMapping("/updateCustomerStatus/{id}/{status}")
    public Result<Boolean> updateCustomerStatus(
            @Parameter(name = "id", description = "用户ID", required = true)
            @PathVariable Long id,
            @Parameter(name = "status", description = "状态：1有效，2禁用", required = true)
            @PathVariable Integer status) {
        return customerInfoFeignClient.updateCustomerStatus(id, status);
    }
}

