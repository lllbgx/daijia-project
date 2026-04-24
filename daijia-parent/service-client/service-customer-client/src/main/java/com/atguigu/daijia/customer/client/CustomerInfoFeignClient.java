package com.atguigu.daijia.customer.client;

import com.atguigu.daijia.common.result.Result;
import com.atguigu.daijia.model.entity.customer.CustomerInfo;
import com.atguigu.daijia.model.form.customer.UpdateWxPhoneForm;
import com.atguigu.daijia.model.vo.base.PageVo;
import com.atguigu.daijia.model.vo.customer.CustomerLoginVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "service-customer")
public interface CustomerInfoFeignClient {

    @GetMapping("/customer/info/login/{code}")
    public Result<Long> login(@PathVariable String code);

    @GetMapping("/customer/info/getCustomerLoginInfo/{customerId}")
    Result<CustomerLoginVo> getCustomerLoginInfo(@PathVariable("customerId") Long customerId);

    /**
     * 更新客户微信手机号码
     * @param updateWxPhoneForm
     * @return
     */
    @PostMapping("/customer/info/updateWxPhoneNumber")
    Result<Boolean> updateWxPhoneNumber(@RequestBody UpdateWxPhoneForm updateWxPhoneForm);

    /**
     * 获取客户OpenId
     * @param customerId
     * @return
     */
    @GetMapping("/customer/info/getCustomerOpenId/{customerId}")
    Result<String> getCustomerOpenId(@PathVariable("customerId") Long customerId);

    // ==================== 管理端Feign接口 ====================

    /**
     * 分页查询用户信息（管理端）
     */
    @GetMapping("/customer/info/mgr/findCustomerInfoPage/{page}/{limit}")
    Result<PageVo<CustomerInfo>> findCustomerInfoPage(@PathVariable("page") Long page, @PathVariable("limit") Long limit);

    /**
     * 根据昵称分页查询用户信息（管理端）
     */
    @GetMapping("/customer/info/mgr/findCustomerInfoPageByName/{page}/{limit}")
    Result<PageVo<CustomerInfo>> findCustomerInfoPageByName(@PathVariable("page") Long page, @PathVariable("limit") Long limit, @RequestParam("nickname") String nickname);

    /**
     * 更新用户状态（管理端）
     */
    @GetMapping("/customer/info/mgr/updateCustomerStatus/{id}/{status}")
    Result<Boolean> updateCustomerStatus(@PathVariable("id") Long id, @PathVariable("status") Integer status);
}