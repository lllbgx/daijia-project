package com.atguigu.daijia.customer.controller;

import com.atguigu.daijia.common.result.Result;
import com.atguigu.daijia.customer.service.CustomerInfoService;
import com.atguigu.daijia.model.entity.customer.CustomerInfo;
import com.atguigu.daijia.model.form.customer.UpdateWxPhoneForm;
import com.atguigu.daijia.model.vo.base.PageVo;
import com.atguigu.daijia.model.vo.customer.CustomerLoginVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "用户API接口管理")
@RestController
@RequestMapping("/customer/info")
@SuppressWarnings({"unchecked", "rawtypes"})
public class CustomerInfoController {

	@Autowired
	private CustomerInfoService customerInfoService;


	@Operation(summary = "获取客户登录信息")
	@GetMapping("/getCustomerLoginInfo/{customerId}")
	public Result<CustomerLoginVo> getCustomerLoginInfo(@PathVariable Long customerId) {
		CustomerLoginVo customerLoginVo = customerInfoService.getCustomerInfo(customerId);
		return Result.ok(customerLoginVo);
	}

	//微信小程序登录接口
	@Operation(summary = "小程序授权登录")
	@GetMapping("/login/{code}")
	public Result<Long> login(@PathVariable String code) {
		return Result.ok(customerInfoService.login(code));
	}

	@Operation(summary = "更新客户微信手机号码")
	@PostMapping("/updateWxPhoneNumber")
	public Result<Boolean> updateWxPhoneNumber(@RequestBody UpdateWxPhoneForm updateWxPhoneForm) {
		return Result.ok(customerInfoService.updateWxPhoneNumber(updateWxPhoneForm));
	}

	@Operation(summary = "获取客户OpenId")
	@GetMapping("/getCustomerOpenId/{customerId}")
	public Result<String> getCustomerOpenId(@PathVariable Long customerId) {
		return Result.ok(customerInfoService.getCustomerOpenId(customerId));
	}

	// ==================== 管理端API ====================

	@Operation(summary = "分页查询用户信息（管理端）")
	@GetMapping("/mgr/findCustomerInfoPage/{page}/{limit}")
	public Result<PageVo<CustomerInfo>> findCustomerInfoPage(
			@Parameter(name = "page", description = "当前页码", required = true)
			@PathVariable Long page,
			@Parameter(name = "limit", description = "每页记录数", required = true)
			@PathVariable Long limit) {
		return Result.ok(customerInfoService.findCustomerInfoPage(page, limit));
	}

	@Operation(summary = "根据昵称分页查询用户信息（管理端）")
	@GetMapping("/mgr/findCustomerInfoPageByName/{page}/{limit}")
	public Result<PageVo<CustomerInfo>> findCustomerInfoPageByName(
			@Parameter(name = "page", description = "当前页码", required = true)
			@PathVariable Long page,
			@Parameter(name = "limit", description = "每页记录数", required = true)
			@PathVariable Long limit,
			@Parameter(name = "nickname", description = "用户昵称", required = false)
			@RequestParam(required = false) String nickname) {
		return Result.ok(customerInfoService.findCustomerInfoPageByName(page, limit, nickname));
	}

	@Operation(summary = "更新用户状态（管理端）")
	@GetMapping("/mgr/updateCustomerStatus/{id}/{status}")
	public Result<Boolean> updateCustomerStatus(
			@Parameter(name = "id", description = "用户ID", required = true)
			@PathVariable Long id,
			@Parameter(name = "status", description = "状态：1有效，2禁用", required = true)
			@PathVariable Integer status) {
		return Result.ok(customerInfoService.updateCustomerStatus(id, status));
	}
}

