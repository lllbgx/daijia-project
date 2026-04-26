package com.atguigu.daijia.driver.controller;

import com.atguigu.daijia.common.result.Result;
import com.atguigu.daijia.driver.service.DriverAccountService;
import com.atguigu.daijia.model.entity.driver.DriverAccount;
import com.atguigu.daijia.model.entity.driver.DriverAccountDetail;
import com.atguigu.daijia.model.form.driver.TransferForm;
import com.atguigu.daijia.model.vo.base.PageVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
    private DriverAccountService driverAccountService;

    @Operation(summary = "转账")
    @PostMapping("/transfer")
    public Result<Boolean> transfer(@RequestBody TransferForm transferForm) {
        return Result.ok(driverAccountService.transfer(transferForm));
    }

    @Operation(summary = "获取账户信息")
    @GetMapping("/info/{driverId}")
    public Result<DriverAccount> getAccountInfo(
            @Parameter(name = "driverId", description = "司机ID", required = true)
            @PathVariable Long driverId) {
        return Result.ok(driverAccountService.getByDriverId(driverId));
    }

    @Operation(summary = "获取账户明细列表")
    @GetMapping("/detail/{driverId}/{page}/{limit}")
    public Result<PageVo<DriverAccountDetail>> getAccountDetailPage(
            @Parameter(name = "driverId", description = "司机ID", required = true)
            @PathVariable Long driverId,
            @Parameter(name = "page", description = "当前页码", required = true)
            @PathVariable Long page,
            @Parameter(name = "limit", description = "每页记录数", required = true)
            @PathVariable Long limit) {
        return Result.ok(driverAccountService.getDetailPage(driverId, page, limit));
    }

    @Operation(summary = "锁定收入")
    @GetMapping("/lockIncome/{driverId}/{amount}/{tradeNo}/{content}")
    public Result<Boolean> lockIncome(
            @Parameter(name = "driverId", description = "司机ID", required = true)
            @PathVariable Long driverId,
            @Parameter(name = "amount", description = "金额", required = true)
            @PathVariable BigDecimal amount,
            @Parameter(name = "tradeNo", description = "交易编号", required = true)
            @PathVariable String tradeNo,
            @Parameter(name = "content", description = "描述", required = true)
            @PathVariable String content) {
        return Result.ok(driverAccountService.lockIncome(driverId, amount, tradeNo, content));
    }

    @Operation(summary = "解锁收入")
    @GetMapping("/unlockIncome/{driverId}/{amount}/{tradeNo}/{content}")
    public Result<Boolean> unlockIncome(
            @Parameter(name = "driverId", description = "司机ID", required = true)
            @PathVariable Long driverId,
            @Parameter(name = "amount", description = "金额", required = true)
            @PathVariable BigDecimal amount,
            @Parameter(name = "tradeNo", description = "交易编号", required = true)
            @PathVariable String tradeNo,
            @Parameter(name = "content", description = "描述", required = true)
            @PathVariable String content) {
        return Result.ok(driverAccountService.unlockIncome(driverId, amount, tradeNo, content));
    }

    @Operation(summary = "提现")
    @PostMapping("/withdraw")
    public Result<Boolean> withdraw(@RequestBody TransferForm transferForm) {
        return Result.ok(driverAccountService.withdraw(
                transferForm.getDriverId(),
                transferForm.getAmount(),
                transferForm.getTradeNo(),
                transferForm.getContent()
        ));
    }
}