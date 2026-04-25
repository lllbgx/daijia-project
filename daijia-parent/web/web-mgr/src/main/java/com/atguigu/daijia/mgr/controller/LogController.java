package com.atguigu.daijia.mgr.controller;

import com.atguigu.daijia.common.result.Result;
import com.atguigu.daijia.customer.client.CustomerLoginLogFeignClient;
import com.atguigu.daijia.driver.client.DriverLoginLogFeignClient;
import com.atguigu.daijia.model.entity.customer.CustomerLoginLog;
import com.atguigu.daijia.model.entity.driver.DriverLoginLog;
import com.atguigu.daijia.model.form.customer.LoginLogQueryForm;
import com.atguigu.daijia.model.vo.base.PageVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "日志管理API接口")
@RestController
@RequestMapping("/mgr/log")
@SuppressWarnings({"unchecked", "rawtypes"})
public class LogController {

    @Autowired
    private CustomerLoginLogFeignClient customerLoginLogFeignClient;

    @Autowired
    private DriverLoginLogFeignClient driverLoginLogFeignClient;

    @Operation(summary = "分页查询用户登录日志")
    @PostMapping("/findCustomerLoginLogPage/{page}/{limit}")
    public Result<PageVo<CustomerLoginLog>> findCustomerLoginLogPage(
            @Parameter(name = "page", description = "当前页码", required = true)
            @PathVariable Long page,
            @Parameter(name = "limit", description = "每页记录数", required = true)
            @PathVariable Long limit,
            @RequestBody LoginLogQueryForm queryForm) {
        return customerLoginLogFeignClient.findLoginLogPage(page, limit, queryForm);
    }

    @Operation(summary = "分页查询司机登录日志")
    @PostMapping("/findDriverLoginLogPage/{page}/{limit}")
    public Result<PageVo<DriverLoginLog>> findDriverLoginLogPage(
            @Parameter(name = "page", description = "当前页码", required = true)
            @PathVariable Long page,
            @Parameter(name = "limit", description = "每页记录数", required = true)
            @PathVariable Long limit,
            @RequestBody com.atguigu.daijia.model.form.driver.LoginLogQueryForm queryForm) {
        return driverLoginLogFeignClient.findLoginLogPage(page, limit, queryForm);
    }
}
