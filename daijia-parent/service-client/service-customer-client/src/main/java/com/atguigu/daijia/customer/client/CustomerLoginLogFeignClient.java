package com.atguigu.daijia.customer.client;

import com.atguigu.daijia.common.result.Result;
import com.atguigu.daijia.model.entity.customer.CustomerLoginLog;
import com.atguigu.daijia.model.form.customer.LoginLogQueryForm;
import com.atguigu.daijia.model.vo.base.PageVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "service-customer")
public interface CustomerLoginLogFeignClient {

    /**
     * 分页查询用户登录日志（管理端）
     */
    @PostMapping("/customer/login/log/mgr/findLoginLogPage/{page}/{limit}")
    Result<PageVo<CustomerLoginLog>> findLoginLogPage(
            @PathVariable("page") Long page,
            @PathVariable("limit") Long limit,
            @RequestBody LoginLogQueryForm queryForm);
}
