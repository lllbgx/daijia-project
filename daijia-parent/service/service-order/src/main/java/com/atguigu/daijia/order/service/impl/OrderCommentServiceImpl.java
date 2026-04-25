package com.atguigu.daijia.order.service.impl;

import com.atguigu.daijia.common.util.AuthContextHolder;
import com.atguigu.daijia.model.entity.order.OrderComment;
import com.atguigu.daijia.model.entity.order.OrderInfo;
import com.atguigu.daijia.model.enums.OrderStatus;
import com.atguigu.daijia.model.form.order.OrderCommentForm;
import com.atguigu.daijia.model.vo.order.OrderCommentVo;
import com.atguigu.daijia.model.vo.order.DriverRatingVo;
import com.atguigu.daijia.order.mapper.OrderCommentMapper;
import com.atguigu.daijia.order.mapper.OrderInfoMapper;
import com.atguigu.daijia.order.service.OrderCommentService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

@Service
public class OrderCommentServiceImpl extends ServiceImpl<OrderCommentMapper, OrderComment> implements OrderCommentService {

    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Override
    @Transactional
    public Boolean submitOrderComment(OrderCommentForm orderCommentForm) {
//        Long customerId = AuthContextHolder.getUserId();

        // 验证订单是否存在且已付款
        OrderInfo orderInfo = orderInfoMapper.selectById(orderCommentForm.getOrderId());
        if (orderInfo == null) {
            throw new RuntimeException("订单不存在");
        }

        // 检查订单状态是否为已付款（PAID = 8）
        Integer orderStatus = orderInfo.getStatus();
        if (!OrderStatus.PAID.getStatus().equals(orderStatus)) {
            throw new RuntimeException("订单未完成，无法评价");
        }

        // 检查是否已评价
        LambdaQueryWrapper<OrderComment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderComment::getOrderId, orderCommentForm.getOrderId())
                   .eq(OrderComment::getCustomerId, orderCommentForm.getCustomerId());
        Long count = this.baseMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new RuntimeException("您已经评价过该订单");
        }

        // 保存评价
        OrderComment orderComment = new OrderComment();
        BeanUtils.copyProperties(orderCommentForm, orderComment);
        orderComment.setDriverId(orderInfo.getDriverId());
        orderComment.setStatus(1); // 1未申诉
        orderComment.setCreateTime(new Date(System.currentTimeMillis()));
        orderComment.setUpdateTime(new Date(System.currentTimeMillis()));
        this.save(orderComment);

        // 更新订单状态为已完成（FINISH = 9）
        OrderInfo updateOrder = new OrderInfo();
        updateOrder.setId(orderCommentForm.getOrderId());
        updateOrder.setStatus(OrderStatus.FINISH.getStatus());
        orderInfoMapper.updateById(updateOrder);

        return true;
    }

    @Override
    public OrderCommentVo getOrderComment(Long orderId) {
        LambdaQueryWrapper<OrderComment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderComment::getOrderId, orderId);
        OrderComment orderComment = this.getOne(queryWrapper);

        if (orderComment == null) {
            return null;
        }

        OrderCommentVo orderCommentVo = new OrderCommentVo();
        BeanUtils.copyProperties(orderComment, orderCommentVo);
        return orderCommentVo;
    }

    @Override
    public DriverRatingVo getDriverRating(Long driverId) {
        LambdaQueryWrapper<OrderComment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderComment::getDriverId, driverId);

        // 查询所有评价
        java.util.List<OrderComment> commentList = this.list(queryWrapper);

        if (commentList.isEmpty()) {
            return new DriverRatingVo(0, 0.0, 0);
        }

        // 计算平均评分
        double totalScore = 0;
        int totalCount = commentList.size();

        for (OrderComment comment : commentList) {
            totalScore += comment.getRate();
        }

        double avgScore = totalScore / totalCount;

        return new DriverRatingVo(totalCount, avgScore, 0);
    }
}