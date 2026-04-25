package com.atguigu.daijia.driver.controller;

import com.atguigu.daijia.common.result.Result;
import com.atguigu.daijia.driver.service.DriverInfoService;
import com.atguigu.daijia.model.entity.driver.DriverInfo;
import com.atguigu.daijia.model.entity.driver.DriverSet;
import com.atguigu.daijia.model.form.driver.DriverFaceModelForm;
import com.atguigu.daijia.model.form.driver.DriverQueryForm;
import com.atguigu.daijia.model.form.driver.UpdateDriverAuthInfoForm;
import com.atguigu.daijia.model.vo.base.PageVo;
import com.atguigu.daijia.model.vo.driver.DriverAuthInfoVo;
import com.atguigu.daijia.model.vo.driver.DriverInfoVo;
import com.atguigu.daijia.model.vo.driver.DriverLoginVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

@Slf4j
@Tag(name = "司机API接口管理")
@RestController
@RequestMapping(value="/driver/info")
@SuppressWarnings({"unchecked", "rawtypes"})
public class DriverInfoController {

    @Autowired
    private DriverInfoService driverInfoService;

    @Operation(summary = "小程序授权登录")
    @GetMapping("/login/{code}")
    public Result<Long> login(@PathVariable String code) {
        return Result.ok(driverInfoService.login(code));
    }

    @Operation(summary = "获取司机登录信息")
    @GetMapping("/getDriverLoginInfo/{driverId}")
    public Result<DriverLoginVo> getDriverInfo(@PathVariable Long driverId) {
        DriverLoginVo driverLoginVo = driverInfoService.getDriverInfo(driverId);
        return Result.ok(driverLoginVo);
    }

    @Operation(summary = "获取司机认证信息")
    @GetMapping("/getDriverAuthInfo/{driverId}")
    public Result<DriverAuthInfoVo> getDriverAuthInfo(@PathVariable Long driverId) {
        DriverAuthInfoVo driverAuthInfoVo = driverInfoService.getDriverAuthInfo(driverId);
        return Result.ok(driverAuthInfoVo);
    }

    //更新司机认证信息
    @Operation(summary = "更新司机认证信息")
    @PostMapping("/updateDriverAuthInfo")
    public Result<Boolean> updateDriverAuthInfo(@RequestBody UpdateDriverAuthInfoForm updateDriverAuthInfoForm) {
        Boolean isSuccess = driverInfoService.updateDriverAuthInfo(updateDriverAuthInfoForm);
        return Result.ok(isSuccess);
    }

    //创建司机人脸模型
    @Operation(summary = "创建司机人脸模型")
    @PostMapping("/creatDriverFaceModel")
    public Result<Boolean> creatDriverFaceModel(@RequestBody DriverFaceModelForm driverFaceModelForm) {
        Boolean isSuccess = driverInfoService.creatDriverFaceModel(driverFaceModelForm);
        return Result.ok(isSuccess);
    }

    @Operation(summary = "获取司机设置信息")
    @GetMapping("/getDriverSet/{driverId}")
    public Result<DriverSet> getDriverSet(@PathVariable Long driverId) {
        return Result.ok(driverInfoService.getDriverSet(driverId));
    }

    @Operation(summary = "判断司机当日是否进行过人脸识别")
    @GetMapping("/isFaceRecognition/{driverId}")
    Result<Boolean> isFaceRecognition(@PathVariable("driverId") Long driverId) {
        return Result.ok(driverInfoService.isFaceRecognition(driverId));
    }

    @Operation(summary = "验证司机人脸")
    @PostMapping("/verifyDriverFace")
    public Result<Boolean> verifyDriverFace(@RequestBody DriverFaceModelForm driverFaceModelForm) {
        return Result.ok(driverInfoService.verifyDriverFace(driverFaceModelForm));
    }

    //更新接单状态
    @Operation(summary = "更新接单状态")
    @GetMapping("/updateServiceStatus/{driverId}/{status}")
    public Result<Boolean> updateServiceStatus(@PathVariable Long driverId, @PathVariable Integer status) {
        return Result.ok(driverInfoService.updateServiceStatus(driverId, status));
    }

    @Operation(summary = "获取司机基本信息")
    @GetMapping("/getDriverInfo/{driverId}")
    public Result<DriverInfoVo> getDriverInfoOrder(@PathVariable Long driverId) {
        return Result.ok(driverInfoService.getDriverInfoOrder(driverId));
    }

    @Operation(summary = "获取司机OpenId")
    @GetMapping("/getDriverOpenId/{driverId}")
    public Result<String> getDriverOpenId(@PathVariable Long driverId) {
        return Result.ok(driverInfoService.getDriverOpenId(driverId));
    }

    // ==================== 管理端API ====================

    @Operation(summary = "分页查询司机信息（管理端）")
    @GetMapping("/mgr/findDriverInfoPage/{page}/{limit}")
    public Result<PageVo<DriverInfo>> findDriverInfoPage(
            @Parameter(name = "page", description = "当前页码", required = true)
            @PathVariable Long page,
            @Parameter(name = "limit", description = "每页记录数", required = true)
            @PathVariable Long limit) {
        return Result.ok(driverInfoService.findDriverInfoPage(page, limit));
    }

    @Operation(summary = "根据姓名分页查询司机信息（管理端）")
    @GetMapping("/mgr/findDriverInfoPageByName/{page}/{limit}")
    public Result<PageVo<DriverInfo>> findDriverInfoPageByName(
            @Parameter(name = "page", description = "当前页码", required = true)
            @PathVariable Long page,
            @Parameter(name = "limit", description = "每页记录数", required = true)
            @PathVariable Long limit,
            @Parameter(name = "name", description = "司机姓名", required = false)
            @RequestParam(required = false) String name) {
        return Result.ok(driverInfoService.findDriverInfoPageByName(page, limit, name));
    }

    @Operation(summary = "根据认证状态分页查询司机信息（管理端）")
    @GetMapping("/mgr/findDriverInfoPageByAuthStatus/{page}/{limit}")
    public Result<PageVo<DriverInfo>> findDriverInfoPageByAuthStatus(
            @Parameter(name = "page", description = "当前页码", required = true)
            @PathVariable Long page,
            @Parameter(name = "limit", description = "每页记录数", required = true)
            @PathVariable Long limit,
            @Parameter(name = "authStatus", description = "认证状态", required = false)
            @RequestParam(required = false) Integer authStatus) {
        return Result.ok(driverInfoService.findDriverInfoPageByAuthStatus(page, limit, authStatus));
    }

    @Operation(summary = "更新司机状态（管理端）")
    @GetMapping("/mgr/updateDriverStatus/{id}/{status}")
    public Result<Boolean> updateDriverStatus(
            @Parameter(name = "id", description = "司机ID", required = true)
            @PathVariable Long id,
            @Parameter(name = "status", description = "状态：1正常，2禁用", required = true)
            @PathVariable Integer status) {
        return Result.ok(driverInfoService.updateDriverStatus(id, status));
    }

    @Operation(summary = "根据多条件分页查询司机信息（管理端）")
    @PostMapping("/mgr/findDriverInfoPageByCondition/{page}/{limit}")
    public Result<PageVo<DriverInfo>> findDriverInfoPageByCondition(
            @Parameter(name = "page", description = "当前页码", required = true)
            @PathVariable Long page,
            @Parameter(name = "limit", description = "每页记录数", required = true)
            @PathVariable Long limit,
            @RequestBody DriverQueryForm driverQueryForm) {
        Page<DriverInfo> pageParam = new Page<>(page, limit);
        return Result.ok(driverInfoService.findDriverInfoPageByCondition(pageParam, driverQueryForm));
    }
}

