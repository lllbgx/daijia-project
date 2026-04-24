package com.atguigu.daijia.coupon.service;

import com.atguigu.daijia.model.entity.coupon.CouponInfo;
import com.atguigu.daijia.model.form.coupon.UseCouponForm;
import com.atguigu.daijia.model.vo.base.PageVo;
import com.atguigu.daijia.model.vo.coupon.AvailableCouponVo;
import com.atguigu.daijia.model.vo.coupon.NoReceiveCouponVo;
import com.atguigu.daijia.model.vo.coupon.NoUseCouponVo;
import com.atguigu.daijia.model.vo.coupon.UsedCouponVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.util.List;

public interface CouponInfoService extends IService<CouponInfo> {

    PageVo<NoReceiveCouponVo> findNoReceivePage(Page<CouponInfo> pageParam, Long customerId);

    PageVo<NoUseCouponVo> findNoUsePage(Page<CouponInfo> pageParam, Long customerId);

    Boolean receive(Long customerId, Long couponId);

    List<AvailableCouponVo> findAvailableCoupon(Long customerId, BigDecimal orderAmount);

    BigDecimal useCoupon(UseCouponForm useCouponForm);

    PageVo<UsedCouponVo> findUsedPage(Page<CouponInfo> pageParam, Long customerId);

    Boolean updateCouponStatus(Long customerCouponId, Long orderId);

    // ==================== 管理端API ====================

    /**
     * 分页查询优惠券信息
     */
    PageVo<CouponInfo> findCouponInfoPage(Long page, Long limit);

    /**
     * 根据ID获取优惠券信息
     */
    CouponInfo getCouponInfoById(Long id);

    /**
     * 根据名称分页查询优惠券信息
     */
    PageVo<CouponInfo> findCouponInfoPageByName(Long page, Long limit, String name);

    /**
     * 根据状态分页查询优惠券信息
     */
    PageVo<CouponInfo> findCouponInfoPageByStatus(Long page, Long limit, Integer status);

    /**
     * 更新优惠券状态
     */
    Boolean updateCouponStatusById(Long id, Integer status);
}
