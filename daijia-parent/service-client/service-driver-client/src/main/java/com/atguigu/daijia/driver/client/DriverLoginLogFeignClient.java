package com.atguigu.daijia.driver.client;

import com.atguigu.daijia.common.result.Result;
import com.atguigu.daijia.model.entity.driver.DriverLoginLog;
import com.atguigu.daijia.model.form.driver.LoginLogQueryForm;
import com.atguigu.daijia.model.vo.base.PageVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "service-driver")
public interface DriverLoginLogFeignClient {

    /**
     * 分页查询司机登录日志（管理端）
     */
    @PostMapping("/driver/login/log/mgr/findLoginLogPage/{page}/{limit}")
    Result<PageVo<DriverLoginLog>> findLoginLogPage(
            @PathVariable("page") Long page,
            @PathVariable("limit") Long limit,
            @RequestBody LoginLogQueryForm queryForm);
}
