package com.atguigu.daijia.coupon.client;

import com.atguigu.daijia.common.result.Result;
import com.atguigu.daijia.model.entity.coupon.CouponInfo;
import com.atguigu.daijia.model.form.coupon.CouponQueryForm;
import com.atguigu.daijia.model.form.coupon.UseCouponForm;
import com.atguigu.daijia.model.vo.base.PageVo;
import com.atguigu.daijia.model.vo.coupon.AvailableCouponVo;
import com.atguigu.daijia.model.vo.coupon.NoReceiveCouponVo;
import com.atguigu.daijia.model.vo.coupon.NoUseCouponVo;
import com.atguigu.daijia.model.vo.coupon.UsedCouponVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;


@FeignClient(value = "service-coupon")
public interface CouponFeignClient {

    /**
     * 查询未领取优惠券分页列表
     * @param customerId
     * @param page
     * @param limit
     * @return
     */
    @GetMapping("/coupon/info/findNoReceivePage/{customerId}/{page}/{limit}")
    Result<PageVo<NoReceiveCouponVo>> findNoReceivePage(
            @PathVariable("customerId") Long customerId,
            @PathVariable("page") Long page,
            @PathVariable("limit") Long limit);

    /**
     * 查询未使用优惠券分页列表
     * @param customerId
     * @param page
     * @param limit
     * @return
     */
    @GetMapping("/coupon/info/findNoUsePage/{customerId}/{page}/{limit}")
    Result<PageVo<NoUseCouponVo>> findNoUsePage(
            @PathVariable("customerId") Long customerId,
            @PathVariable("page") Long page,
            @PathVariable("limit") Long limit);

    /**
     * 领取优惠券
     * @param customerId
     * @param couponId
     * @return
     */
    @GetMapping("/coupon/info/receive/{customerId}/{couponId}")
    Result<Boolean> receive(@PathVariable("customerId") Long customerId, @PathVariable("couponId") Long couponId);

    /**
     * 获取未使用的最佳优惠券信息
     * @param customerId
     * @param orderAmount
     * @return
     */
    @GetMapping("/coupon/info/findAvailableCoupon/{customerId}/{orderAmount}")
    Result<List<AvailableCouponVo>> findAvailableCoupon(@PathVariable("customerId") Long customerId, @PathVariable("orderAmount") BigDecimal orderAmount);

    /**
     * 使用优惠券
     * @param useCouponForm
     * @return
     */
    @PostMapping("/coupon/info/useCoupon")
    Result<BigDecimal> useCoupon(@RequestBody UseCouponForm useCouponForm);


    /**
     * 查询已使用优惠券分页列表
     * @param customerId
     * @param page
     * @param limit
     * @return
     */
    @GetMapping("/coupon/info/findUsedPage/{customerId}/{page}/{limit}")
    Result<PageVo<UsedCouponVo>> findUsedPage(
            @PathVariable("customerId") Long customerId,
            @PathVariable("page") Long page,
            @PathVariable("limit") Long limit);

    /**
     * 更新优惠券状态为已使用
     * @param customerCouponId
     * @param orderId
     * @return
     */
    @PostMapping("/coupon/info/updateCouponStatus/{customerCouponId}/{orderId}")
    Result<Boolean> updateCouponStatus(@PathVariable("customerCouponId") Long customerCouponId,
                                     @PathVariable("orderId") Long orderId);

    // ==================== 管理端Feign接口 ====================

    /**
     * 分页查询优惠券信息（管理端）
     */
    @GetMapping("/coupon/info/mgr/findCouponInfoPage/{page}/{limit}")
    Result<PageVo<CouponInfo>> findCouponInfoPage(@PathVariable("page") Long page, @PathVariable("limit") Long limit);

    /**
     * 根据ID获取优惠券信息（管理端）
     */
    @GetMapping("/coupon/info/mgr/getCouponInfoById/{id}")
    Result<CouponInfo> getCouponInfoById(@PathVariable("id") Long id);

    /**
     * 根据名称分页查询优惠券信息（管理端）
     */
    @GetMapping("/coupon/info/mgr/findCouponInfoPageByName/{page}/{limit}")
    Result<PageVo<CouponInfo>> findCouponInfoPageByName(@PathVariable("page") Long page, @PathVariable("limit") Long limit, @RequestParam("name") String name);

    /**
     * 根据状态分页查询优惠券信息（管理端）
     */
    @GetMapping("/coupon/info/mgr/findCouponInfoPageByStatus/{page}/{limit}")
    Result<PageVo<CouponInfo>> findCouponInfoPageByStatus(@PathVariable("page") Long page, @PathVariable("limit") Long limit, @RequestParam("status") Integer status);

    /**
     * 更新优惠券状态（管理端）
     */
    @GetMapping("/coupon/info/mgr/updateCouponStatusById/{id}/{status}")
    Result<Boolean> updateCouponStatusById(@PathVariable("id") Long id, @PathVariable("status") Integer status);

    /**
     * 根据多条件分页查询优惠券信息（管理端）
     */
    @PostMapping("/coupon/info/mgr/findCouponInfoPageByCondition/{page}/{limit}")
    Result<PageVo<CouponInfo>> findCouponInfoPageByCondition(@PathVariable("page") Long page, @PathVariable("limit") Long limit, @RequestBody CouponQueryForm couponQueryForm);

    /**
     * 新增优惠券（管理端）
     */
    @PostMapping("/coupon/info/mgr/addCoupon")
    Result<Boolean> addCoupon(@RequestBody CouponInfo couponInfo);

    /**
     * 删除优惠券（管理端）
     */
    @DeleteMapping("/coupon/info/mgr/deleteCoupon/{id}")
    Result<Boolean> deleteCoupon(@PathVariable("id") Long id);

}