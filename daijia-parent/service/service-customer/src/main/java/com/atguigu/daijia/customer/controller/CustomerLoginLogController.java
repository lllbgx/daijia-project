package com.atguigu.daijia.customer.controller;

import com.atguigu.daijia.common.result.Result;
import com.atguigu.daijia.customer.service.CustomerLoginLogService;
import com.atguigu.daijia.model.entity.customer.CustomerLoginLog;
import com.atguigu.daijia.model.form.customer.LoginLogQueryForm;
import com.atguigu.daijia.model.vo.base.PageVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "用户登录日志管理")
@RestController
@RequestMapping("/customer/login/log")
@SuppressWarnings({"unchecked", "rawtypes"})
public class CustomerLoginLogController {

    @Autowired
    private CustomerLoginLogService customerLoginLogService;

    @Operation(summary = "分页查询用户登录日志（管理端）")
    @PostMapping("/mgr/findLoginLogPage/{page}/{limit}")
    public Result<PageVo<CustomerLoginLog>> findLoginLogPage(
            @Parameter(name = "page", description = "当前页码", required = true)
            @PathVariable Long page,
            @Parameter(name = "limit", description = "每页记录数", required = true)
            @PathVariable Long limit,
            @RequestBody LoginLogQueryForm queryForm) {
        Page<CustomerLoginLog> pageParam = new Page<>(page, limit);
        return Result.ok(customerLoginLogService.findLoginLogPage(pageParam, queryForm));
    }
}
