package com.atguigu.daijia.order.service;

import com.atguigu.daijia.model.form.order.OrderCommentForm;
import com.atguigu.daijia.model.vo.order.OrderCommentVo;
import com.atguigu.daijia.model.vo.order.DriverRatingVo;

public interface OrderCommentService {

    /**
     * 提交订单评价
     * @param orderCommentForm 评价表单
     * @return 是否提交成功
     */
    Boolean submitOrderComment(OrderCommentForm orderCommentForm);

    /**
     * 获取订单评价信息
     * @param orderId 订单ID
     * @return 评价信息
     */
    OrderCommentVo getOrderComment(Long orderId);

    /**
     * 获取司机评价统计
     * @param driverId 司机ID
     * @return 司机评分统计
     */
    DriverRatingVo getDriverRating(Long driverId);
}