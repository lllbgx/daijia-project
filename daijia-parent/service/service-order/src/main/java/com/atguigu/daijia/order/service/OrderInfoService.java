package com.atguigu.daijia.order.service;

import com.atguigu.daijia.model.entity.order.OrderInfo;
import com.atguigu.daijia.model.form.order.OrderInfoForm;
import com.atguigu.daijia.model.form.order.OrderQueryForm;
import com.atguigu.daijia.model.form.order.StartDriveForm;
import com.atguigu.daijia.model.form.order.UpdateOrderBillForm;
import com.atguigu.daijia.model.form.order.UpdateOrderCartForm;
import com.atguigu.daijia.model.vo.base.PageVo;
import com.atguigu.daijia.model.vo.order.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;

public interface OrderInfoService extends IService<OrderInfo> {

    //乘客下单
    Long saveOrderInfo(OrderInfoForm orderInfoForm);

    //根据订单id获取订单状态
    Integer getOrderStatus(Long orderId);

    //司机抢单
    Boolean robNewOrder(Long driverId, Long orderId);

    //乘客端查找当前订单
    CurrentOrderInfoVo searchCustomerCurrentOrder(Long customerId);

    CurrentOrderInfoVo searchDriverCurrentOrder(Long driverId);

    Boolean driverArriveStartLocation(Long orderId, Long driverId);

    Boolean updateOrderCart(UpdateOrderCartForm updateOrderCartForm);

    Boolean startDriver(StartDriveForm startDriveForm);

    Long getOrderNumByTime(String startTime, String endTime);

    Boolean endDrive(UpdateOrderBillForm updateOrderBillForm);

    //获取乘客订单分页列表
    PageVo findCustomerOrderPage(Page<OrderInfo> pageParam, Long customerId);

    PageVo findDriverOrderPage(Page<OrderInfo> pageParam, Long driverId);

    OrderBillVo getOrderBillInfo(Long orderId);

    OrderProfitsharingVo getOrderProfitsharing(Long orderId);

    Boolean sendOrderBillInfo(Long orderId, Long driverId);

    OrderPayVo getOrderPayVo(String orderNo, Long customerId);

    Boolean updateOrderPayStatus(String orderNo);

    OrderRewardVo getOrderRewardFee(String orderNo);

    ////调用方法取消订单
    void orderCancel(long parseLong);

    Boolean updateCouponAmount(Long orderId, BigDecimal couponAmount);

    // 更新订单支付状态和优惠券状态
    Boolean updateOrderPayStatusWithCoupon(String orderNo, Long customerCouponId);

    // ==================== 管理端API ====================

    /**
     * 分页查询订单信息
     */
    PageVo<OrderInfo> findOrderInfoPage(Long page, Long limit);

    /**
     * 根据ID获取订单信息
     */
    OrderInfo getOrderInfoById(Long id);

    /**
     * 根据订单号获取订单信息
     */
    OrderInfo getOrderInfoByOrderNo(String orderNo);

    /**
     * 根据乘客ID分页查询订单信息
     */
    PageVo<OrderInfo> findOrderInfoPageByCustomerId(Long page, Long limit, Long customerId);

    /**
     * 根据司机ID分页查询订单信息
     */
    PageVo<OrderInfo> findOrderInfoPageByDriverId(Long page, Long limit, Long driverId);

    /**
     * 根据订单状态分页查询订单信息
     */
    PageVo<OrderInfo> findOrderInfoPageByStatus(Long page, Long limit, Integer status);

    /**
     * 根据多条件分页查询订单信息
     */
    PageVo<OrderInfo> findOrderInfoPageByCondition(Page<OrderInfo> pageParam, OrderQueryForm orderQueryForm);
}
