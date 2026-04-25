package com.atguigu.daijia.order.client;

import com.atguigu.daijia.common.result.Result;
import com.atguigu.daijia.model.form.order.OrderCommentForm;
import com.atguigu.daijia.model.vo.order.OrderCommentVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "service-order")
public interface OrderCommentFeignClient {

    /**
     * 提交订单评价
     * @param orderCommentForm 评价表单
     * @return 是否提交成功
     */
    @PostMapping("/order/comment/submit")
    Result<Boolean> submitOrderComment(@RequestBody OrderCommentForm orderCommentForm);

    /**
     * 获取订单评价信息
     * @param orderId 订单ID
     * @return 评价信息
     */
    @GetMapping("/order/comment/get/{orderId}")
    Result<OrderCommentVo> getOrderComment(@PathVariable Long orderId);
}