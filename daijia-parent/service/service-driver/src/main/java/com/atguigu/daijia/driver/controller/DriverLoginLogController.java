package com.atguigu.daijia.driver.controller;

import com.atguigu.daijia.common.result.Result;
import com.atguigu.daijia.driver.service.DriverLoginLogService;
import com.atguigu.daijia.model.entity.driver.DriverLoginLog;
import com.atguigu.daijia.model.form.driver.LoginLogQueryForm;
import com.atguigu.daijia.model.vo.base.PageVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "司机登录日志管理")
@RestController
@RequestMapping("/driver/login/log")
@SuppressWarnings({"unchecked", "rawtypes"})
public class DriverLoginLogController {

    @Autowired
    private DriverLoginLogService driverLoginLogService;

    @Operation(summary = "分页查询司机登录日志（管理端）")
    @PostMapping("/mgr/findLoginLogPage/{page}/{limit}")
    public Result<PageVo<DriverLoginLog>> findLoginLogPage(
            @Parameter(name = "page", description = "当前页码", required = true)
            @PathVariable Long page,
            @Parameter(name = "limit", description = "每页记录数", required = true)
            @PathVariable Long limit,
            @RequestBody LoginLogQueryForm queryForm) {
        Page<DriverLoginLog> pageParam = new Page<>(page, limit);
        return Result.ok(driverLoginLogService.findLoginLogPage(pageParam, queryForm));
    }
}
