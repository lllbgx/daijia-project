package com.atguigu.daijia.mgr.controller;

import com.atguigu.daijia.common.result.Result;
import com.atguigu.daijia.model.entity.order.OrderInfo;
import com.atguigu.daijia.model.vo.base.PageVo;
import com.atguigu.daijia.order.client.OrderInfoFeignClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "订单管理API接口")
@RestController
@RequestMapping(value="/mgr/order/info")
@SuppressWarnings({"unchecked", "rawtypes"})
public class OrderInfoController {

    @Autowired
    private OrderInfoFeignClient orderInfoFeignClient;

    @Operation(summary = "分页查询订单信息")
    @GetMapping("/findOrderInfoPage/{page}/{limit}")
    public Result<PageVo<OrderInfo>> findOrderInfoPage(
            @Parameter(name = "page", description = "当前页码", required = true)
            @PathVariable Long page,
            @Parameter(name = "limit", description = "每页记录数", required = true)
            @PathVariable Long limit) {
        return orderInfoFeignClient.findOrderInfoPage(page, limit);
    }

    @Operation(summary = "根据ID获取订单信息")
    @GetMapping("/getOrderInfoById/{id}")
    public Result<OrderInfo> getOrderInfoById(
            @Parameter(name = "id", description = "订单ID", required = true)
            @PathVariable Long id) {
        return orderInfoFeignClient.getOrderInfoById(id);
    }

    @Operation(summary = "根据订单号获取订单信息")
    @GetMapping("/getOrderInfoByOrderNo")
    public Result<OrderInfo> getOrderInfoByOrderNo(
            @Parameter(name = "orderNo", description = "订单号", required = true)
            @RequestParam String orderNo) {
        return orderInfoFeignClient.getOrderInfoByOrderNo(orderNo);
    }

    @Operation(summary = "根据乘客ID分页查询订单信息")
    @GetMapping("/findOrderInfoPageByCustomerId/{page}/{limit}")
    public Result<PageVo<OrderInfo>> findOrderInfoPageByCustomerId(
            @Parameter(name = "page", description = "当前页码", required = true)
            @PathVariable Long page,
            @Parameter(name = "limit", description = "每页记录数", required = true)
            @PathVariable Long limit,
            @Parameter(name = "customerId", description = "乘客ID", required = false)
            @RequestParam(required = false) Long customerId) {
        return orderInfoFeignClient.findOrderInfoPageByCustomerId(page, limit, customerId);
    }

    @Operation(summary = "根据司机ID分页查询订单信息")
    @GetMapping("/findOrderInfoPageByDriverId/{page}/{limit}")
    public Result<PageVo<OrderInfo>> findOrderInfoPageByDriverId(
            @Parameter(name = "page", description = "当前页码", required = true)
            @PathVariable Long page,
            @Parameter(name = "limit", description = "每页记录数", required = true)
            @PathVariable Long limit,
            @Parameter(name = "driverId", description = "司机ID", required = false)
            @RequestParam(required = false) Long driverId) {
        return orderInfoFeignClient.findOrderInfoPageByDriverId(page, limit, driverId);
    }

    @Operation(summary = "根据订单状态分页查询订单信息")
    @GetMapping("/findOrderInfoPageByStatus/{page}/{limit}")
    public Result<PageVo<OrderInfo>> findOrderInfoPageByStatus(
            @Parameter(name = "page", description = "当前页码", required = true)
            @PathVariable Long page,
            @Parameter(name = "limit", description = "每页记录数", required = true)
            @PathVariable Long limit,
            @Parameter(name = "status", description = "订单状态", required = false)
            @RequestParam(required = false) Integer status) {
        return orderInfoFeignClient.findOrderInfoPageByStatus(page, limit, status);
    }
}

