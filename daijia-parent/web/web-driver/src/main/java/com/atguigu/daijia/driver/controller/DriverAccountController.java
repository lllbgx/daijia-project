package com.atguigu.daijia.driver.controller;

import com.atguigu.daijia.common.login.GuiguLogin;
import com.atguigu.daijia.common.result.Result;
import com.atguigu.daijia.common.util.AuthContextHolder;
import com.atguigu.daijia.driver.client.DriverAccountFeignClient;
import com.atguigu.daijia.model.entity.driver.DriverAccount;
import com.atguigu.daijia.model.entity.driver.DriverAccountDetail;
import com.atguigu.daijia.model.form.driver.TransferForm;
import com.atguigu.daijia.model.vo.base.PageVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Slf4j
@Tag(name = "司机账户API接口管理")
@RestController
@RequestMapping(value="/driver/account")
@SuppressWarnings({"unchecked", "rawtypes"})
public class DriverAccountController {

    @Autowired
    private DriverAccountFeignClient driverAccountFeignClient;

    @Operation(summary = "获取账户信息")
    @GuiguLogin
    @GetMapping("/info")
    public Result<DriverAccount> getAccountInfo() {
        Long driverId = AuthContextHolder.getUserId();
        return driverAccountFeignClient.getAccountInfo(driverId);
    }

    @Operation(summary = "获取账户明细列表")
    @GuiguLogin
    @GetMapping("/detail/{page}/{limit}")
    public Result<PageVo<DriverAccountDetail>> getAccountDetailPage(
            @PathVariable Long page,
            @PathVariable Long limit) {
        Long driverId = AuthContextHolder.getUserId();
        return driverAccountFeignClient.getAccountDetailPage(driverId, page, limit);
    }

    @Operation(summary = "提现")
    @GuiguLogin
    @PostMapping("/withdraw")
    public Result<Boolean> withdraw(@RequestBody TransferForm transferForm) {
        transferForm.setDriverId(AuthContextHolder.getUserId());
        return driverAccountFeignClient.withdraw(transferForm);
    }

    @Operation(summary = "锁定收入")
    @GuiguLogin
    @GetMapping("/lockIncome/{amount}/{tradeNo}/{content}")
    public Result<Boolean> lockIncome(
            @PathVariable BigDecimal amount,
            @PathVariable String tradeNo,
            @PathVariable String content) {
        Long driverId = AuthContextHolder.getUserId();
        return driverAccountFeignClient.lockIncome(driverId, amount, tradeNo, content);
    }

    @Operation(summary = "解锁收入")
    @GuiguLogin
    @GetMapping("/unlockIncome/{amount}/{tradeNo}/{content}")
    public Result<Boolean> unlockIncome(
            @PathVariable BigDecimal amount,
            @PathVariable String tradeNo,
            @PathVariable String content) {
        Long driverId = AuthContextHolder.getUserId();
        return driverAccountFeignClient.unlockIncome(driverId, amount, tradeNo, content);
    }
}