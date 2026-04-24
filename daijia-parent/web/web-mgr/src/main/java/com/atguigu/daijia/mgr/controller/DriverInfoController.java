package com.atguigu.daijia.mgr.controller;

import com.atguigu.daijia.common.result.Result;
import com.atguigu.daijia.driver.client.DriverInfoFeignClient;
import com.atguigu.daijia.model.entity.driver.DriverInfo;
import com.atguigu.daijia.model.vo.base.PageVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "司机管理API接口")
@RestController
@RequestMapping(value="/mgr/driver/info")
@SuppressWarnings({"unchecked", "rawtypes"})
public class DriverInfoController {

    @Autowired
    private DriverInfoFeignClient driverInfoFeignClient;

    @Operation(summary = "分页查询司机信息")
    @GetMapping("/findDriverInfoPage/{page}/{limit}")
    public Result<PageVo<DriverInfo>> findDriverInfoPage(
            @Parameter(name = "page", description = "当前页码", required = true)
            @PathVariable Long page,
            @Parameter(name = "limit", description = "每页记录数", required = true)
            @PathVariable Long limit) {
        return driverInfoFeignClient.findDriverInfoPage(page, limit);
    }

    @Operation(summary = "根据姓名分页查询司机信息")
    @GetMapping("/findDriverInfoPageByName/{page}/{limit}")
    public Result<PageVo<DriverInfo>> findDriverInfoPageByName(
            @Parameter(name = "page", description = "当前页码", required = true)
            @PathVariable Long page,
            @Parameter(name = "limit", description = "每页记录数", required = true)
            @PathVariable Long limit,
            @Parameter(name = "name", description = "司机姓名", required = false)
            @RequestParam(required = false) String name) {
        return driverInfoFeignClient.findDriverInfoPageByName(page, limit, name);
    }

    @Operation(summary = "根据认证状态分页查询司机信息")
    @GetMapping("/findDriverInfoPageByAuthStatus/{page}/{limit}")
    public Result<PageVo<DriverInfo>> findDriverInfoPageByAuthStatus(
            @Parameter(name = "page", description = "当前页码", required = true)
            @PathVariable Long page,
            @Parameter(name = "limit", description = "每页记录数", required = true)
            @PathVariable Long limit,
            @Parameter(name = "authStatus", description = "认证状态", required = false)
            @RequestParam(required = false) Integer authStatus) {
        return driverInfoFeignClient.findDriverInfoPageByAuthStatus(page, limit, authStatus);
    }

    @Operation(summary = "更新司机状态")
    @PutMapping("/updateDriverStatus/{id}/{status}")
    public Result<Boolean> updateDriverStatus(
            @Parameter(name = "id", description = "司机ID", required = true)
            @PathVariable Long id,
            @Parameter(name = "status", description = "状态：1正常，2禁用", required = true)
            @PathVariable Integer status) {
        return driverInfoFeignClient.updateDriverStatus(id, status);
    }
}

