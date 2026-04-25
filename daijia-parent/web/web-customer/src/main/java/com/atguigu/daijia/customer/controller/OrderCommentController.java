package com.atguigu.daijia.customer.controller;

import com.atguigu.daijia.common.login.GuiguLogin;
import com.atguigu.daijia.common.result.Result;
import com.atguigu.daijia.common.util.AuthContextHolder;
import com.atguigu.daijia.order.client.OrderCommentFeignClient;
import com.atguigu.daijia.model.form.order.OrderCommentForm;
import com.atguigu.daijia.model.vo.order.OrderCommentVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "订单评价API接口")
@RestController
@RequestMapping("/order/comment")
@SuppressWarnings({"unchecked", "rawtypes"})
public class OrderCommentController {

    @Autowired
    private OrderCommentFeignClient orderCommentFeignClient;

    @Operation(summary = "提交订单评价")
    @GuiguLogin
    @PostMapping("/submit")
    public Result<Boolean> submitOrderComment(@RequestBody OrderCommentForm orderCommentForm) {
        orderCommentForm.setCustomerId(AuthContextHolder.getUserId());
        return orderCommentFeignClient.submitOrderComment(orderCommentForm);
    }

    @Operation(summary = "获取订单评价信息")
    @GuiguLogin
    @GetMapping("/get/{orderId}")
    public Result<OrderCommentVo> getOrderComment(
            @Parameter(name = "orderId", description = "订单ID", required = true)
            @PathVariable Long orderId) {
        return orderCommentFeignClient.getOrderComment(orderId);
    }
}