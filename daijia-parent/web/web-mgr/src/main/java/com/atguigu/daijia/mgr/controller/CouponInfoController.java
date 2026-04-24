package com.atguigu.daijia.mgr.controller;

import com.atguigu.daijia.common.result.Result;
import com.atguigu.daijia.coupon.client.CouponFeignClient;
import com.atguigu.daijia.model.entity.coupon.CouponInfo;
import com.atguigu.daijia.model.vo.base.PageVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "优惠券管理API接口")
@RestController
@RequestMapping(value="/mgr/coupon/info")
@SuppressWarnings({"unchecked", "rawtypes"})
public class CouponInfoController {

    @Autowired
    private CouponFeignClient couponFeignClient;

    @Operation(summary = "分页查询优惠券信息")
    @GetMapping("/findCouponInfoPage/{page}/{limit}")
    public Result<PageVo<CouponInfo>> findCouponInfoPage(
            @Parameter(name = "page", description = "当前页码", required = true)
            @PathVariable Long page,
            @Parameter(name = "limit", description = "每页记录数", required = true)
            @PathVariable Long limit) {
        return couponFeignClient.findCouponInfoPage(page, limit);
    }

    @Operation(summary = "根据ID获取优惠券信息")
    @GetMapping("/getCouponInfoById/{id}")
    public Result<CouponInfo> getCouponInfoById(
            @Parameter(name = "id", description = "优惠券ID", required = true)
            @PathVariable Long id) {
        return couponFeignClient.getCouponInfoById(id);
    }

    @Operation(summary = "根据名称分页查询优惠券信息")
    @GetMapping("/findCouponInfoPageByName/{page}/{limit}")
    public Result<PageVo<CouponInfo>> findCouponInfoPageByName(
            @Parameter(name = "page", description = "当前页码", required = true)
            @PathVariable Long page,
            @Parameter(name = "limit", description = "每页记录数", required = true)
            @PathVariable Long limit,
            @Parameter(name = "name", description = "优惠券名称", required = false)
            @RequestParam(required = false) String name) {
        return couponFeignClient.findCouponInfoPageByName(page, limit, name);
    }

    @Operation(summary = "根据状态分页查询优惠券信息")
    @GetMapping("/findCouponInfoPageByStatus/{page}/{limit}")
    public Result<PageVo<CouponInfo>> findCouponInfoPageByStatus(
            @Parameter(name = "page", description = "当前页码", required = true)
            @PathVariable Long page,
            @Parameter(name = "limit", description = "每页记录数", required = true)
            @PathVariable Long limit,
            @Parameter(name = "status", description = "状态", required = false)
            @RequestParam(required = false) Integer status) {
        return couponFeignClient.findCouponInfoPageByStatus(page, limit, status);
    }

    @Operation(summary = "更新优惠券状态")
    @PutMapping("/updateCouponStatusById/{id}/{status}")
    public Result<Boolean> updateCouponStatusById(
            @Parameter(name = "id", description = "优惠券ID", required = true)
            @PathVariable Long id,
            @Parameter(name = "status", description = "状态：0-未发布，1-已发布，-1-已过期", required = true)
            @PathVariable Integer status) {
        return couponFeignClient.updateCouponStatusById(id, status);
    }
}
