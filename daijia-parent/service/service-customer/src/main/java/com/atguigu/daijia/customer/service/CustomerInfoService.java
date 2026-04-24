package com.atguigu.daijia.customer.service;

import com.atguigu.daijia.model.entity.customer.CustomerInfo;
import com.atguigu.daijia.model.form.customer.UpdateWxPhoneForm;
import com.atguigu.daijia.model.vo.base.PageVo;
import com.atguigu.daijia.model.vo.customer.CustomerLoginVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

public interface CustomerInfoService extends IService<CustomerInfo> {

    //微信小程序登录接口
    Long login(String code);

    //获取客户登录信息
    CustomerLoginVo getCustomerInfo(Long customerId);

    //更新客户微信手机号码
    Boolean updateWxPhoneNumber(UpdateWxPhoneForm updateWxPhoneForm);

    String getCustomerOpenId(Long customerId);

    // ==================== 管理端API ====================

    /**
     * 分页查询用户信息
     */
    PageVo<CustomerInfo> findCustomerInfoPage(Long page, Long limit);

    /**
     * 根据昵称分页查询用户信息
     */
    PageVo<CustomerInfo> findCustomerInfoPageByName(Long page, Long limit, String nickname);

    /**
     * 更新用户状态
     */
    Boolean updateCustomerStatus(Long id, Integer status);
}
