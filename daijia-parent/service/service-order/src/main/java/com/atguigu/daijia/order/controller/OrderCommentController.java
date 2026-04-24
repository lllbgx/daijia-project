package com.atguigu.daijia.order.controller;

import com.atguigu.daijia.common.result.Result;
import com.atguigu.daijia.model.form.order.OrderCommentForm;
import com.atguigu.daijia.model.vo.order.OrderCommentVo;
import com.atguigu.daijia.model.vo.order.DriverRatingVo;
import com.atguigu.daijia.order.service.OrderCommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "订单评价API接口管理")
@RestController
@RequestMapping("/order/comment")
public class OrderCommentController {

    @Autowired
    private OrderCommentService orderCommentService;

    @Operation(summary = "提交订单评价")
    @PostMapping("/submit")
    public Result<Boolean> submitOrderComment(@RequestBody OrderCommentForm orderCommentForm) {
        return Result.ok(orderCommentService.submitOrderComment(orderCommentForm));
    }

    @Operation(summary = "获取订单评价信息")
    @GetMapping("/get/{orderId}")
    public Result<OrderCommentVo> getOrderComment(@PathVariable Long orderId) {
        return Result.ok(orderCommentService.getOrderComment(orderId));
    }

    @Operation(summary = "获取司机评价统计")
    @GetMapping("/driver/rating/{driverId}")
    public Result<DriverRatingVo> getDriverRating(@Parameter(name = "driverId", description = "司机id", required = true)
                                                 @PathVariable Long driverId) {
        return Result.ok(orderCommentService.getDriverRating(driverId));
    }
}