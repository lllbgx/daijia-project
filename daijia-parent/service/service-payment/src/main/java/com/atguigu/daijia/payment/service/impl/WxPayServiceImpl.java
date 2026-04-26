package com.atguigu.daijia.payment.service.impl;

import com.alibaba.fastjson.JSON;
import com.atguigu.daijia.common.constant.MqConst;
import com.atguigu.daijia.common.execption.GuiguException;
import com.atguigu.daijia.common.result.ResultCodeEnum;
import com.atguigu.daijia.common.service.RabbitService;
import com.atguigu.daijia.driver.client.DriverAccountFeignClient;
import com.atguigu.daijia.model.entity.payment.PaymentInfo;
import com.atguigu.daijia.model.enums.TradeType;
import com.atguigu.daijia.model.form.driver.TransferForm;
import com.atguigu.daijia.model.form.payment.PaymentInfoForm;
import com.atguigu.daijia.model.vo.order.OrderProfitsharingVo;
import com.atguigu.daijia.model.vo.order.OrderRewardVo;
import com.atguigu.daijia.model.vo.payment.WxPrepayVo;
import com.atguigu.daijia.order.client.OrderInfoFeignClient;
import com.atguigu.daijia.payment.config.WxPayV3Properties;
import com.atguigu.daijia.payment.mapper.PaymentInfoMapper;
import com.atguigu.daijia.payment.service.WxPayService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

@Service
@Slf4j
public class WxPayServiceImpl implements WxPayService {

    @Autowired
    private PaymentInfoMapper paymentInfoMapper;

    @Autowired
    private WxPayV3Properties wxPayV3Properties;

    @Autowired
    private RabbitService rabbitService;

    // ==================== 【核心】创建支付 → 直接模拟成功，不调用微信 ====================
    @Override
    public WxPrepayVo createWxPayment(PaymentInfoForm paymentInfoForm) {
        try {
            // 1. 保存支付记录
            LambdaQueryWrapper<PaymentInfo> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(PaymentInfo::getOrderNo, paymentInfoForm.getOrderNo());
            PaymentInfo paymentInfo = paymentInfoMapper.selectOne(wrapper);
            if (paymentInfo == null) {
                paymentInfo = new PaymentInfo();
                BeanUtils.copyProperties(paymentInfoForm, paymentInfo);
                paymentInfo.setPaymentStatus(0);
                paymentInfoMapper.insert(paymentInfo);
            }

            // 2. 直接构造模拟支付信息（完全跳过微信）
            WxPrepayVo wxPrepayVo = new WxPrepayVo();

            wxPrepayVo.setAppId(wxPayV3Properties.getAppid());

            wxPrepayVo.setTimeStamp(String.valueOf(System.currentTimeMillis()));

            wxPrepayVo.setNonceStr("abc123def456ghi789");

            wxPrepayVo.setPackageVal("prepay_id=wx1234567890abcdef1234567890abcdef");

            wxPrepayVo.setSignType("MD5");

            wxPrepayVo.setPaySign("3f61fa0a320f9a0c44e927f04cd1245a");

            // 3. 直接模拟支付成功！
            this.handlePaymentMock(paymentInfoForm.getOrderNo());

            return wxPrepayVo;
        } catch (Exception e) {
            throw new GuiguException(ResultCodeEnum.DATA_ERROR);
        }
    }

    // ==================== 查询支付状态 → 直接返回已支付 ====================
    @Override
    public Boolean queryPayStatus(String orderNo) {
        return true;
    }

    // ==================== 微信回调 → 直接模拟处理成功 ====================
    @Override
    public void wxnotify(HttpServletRequest request) {
        // 模拟回调处理成功
    }

    @Autowired
    private OrderInfoFeignClient orderInfoFeignClient;

    @Autowired
    private DriverAccountFeignClient driverAccountFeignClient;

    // 支付成功后续处理
    @Override
    public void handleOrder(String orderNo) {
        try {
            orderInfoFeignClient.updateOrderPayStatus(orderNo);
            OrderRewardVo orderRewardVo = orderInfoFeignClient.getOrderRewardFee(orderNo).getData();
            if (orderRewardVo != null && orderRewardVo.getRewardFee().doubleValue() > 0) {//系统奖励费大于0
                TransferForm transferForm = new TransferForm();
                transferForm.setTradeNo(orderNo);
                transferForm.setTradeType(TradeType.REWARD.getType());
                transferForm.setContent(TradeType.REWARD.getContent());
                transferForm.setAmount(orderRewardVo.getRewardFee());
                transferForm.setDriverId(orderRewardVo.getDriverId());
                driverAccountFeignClient.transfer(transferForm);
            }

            // 支付成功，解锁司机收入（1202-解锁）
            OrderProfitsharingVo profitsharingVo = orderInfoFeignClient.getOrderProfitsharingByOrderNo(orderNo).getData();
            if (profitsharingVo != null && profitsharingVo.getDriverIncome() != null && profitsharingVo.getDriverIncome().doubleValue() > 0) {
                driverAccountFeignClient.unlockIncome(
                        profitsharingVo.getDriverId(),
                        profitsharingVo.getDriverIncome(),
                        orderNo,
                        "用户支付成功，收入解锁"
                );
            }
        } catch (Exception e) {
            log.error("支付后续处理失败", e);
        }
    }

    // ==================== 模拟支付成功处理 ====================
    public void handlePaymentMock(String orderNo) {
        LambdaQueryWrapper<PaymentInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(PaymentInfo::getOrderNo, orderNo);
        PaymentInfo paymentInfo = paymentInfoMapper.selectOne(wrapper);
        if (paymentInfo == null || paymentInfo.getPaymentStatus() == 1) {
            return;
        }

        paymentInfo.setPaymentStatus(1);
        paymentInfo.setTransactionId("mock_transaction_id");
        paymentInfo.setCallbackTime(new Date());
        paymentInfo.setCallbackContent("mock success");
        paymentInfoMapper.updateById(paymentInfo);

        // 发送MQ更新订单
        rabbitService.sendMessage(MqConst.EXCHANGE_ORDER, MqConst.ROUTING_PAY_SUCCESS, orderNo);
    }

    // 原微信处理（废弃不用）
    public void handlePayment(Object transaction) {
    }
}