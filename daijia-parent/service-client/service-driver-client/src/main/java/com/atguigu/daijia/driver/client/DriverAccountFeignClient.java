package com.atguigu.daijia.driver.client;

import com.atguigu.daijia.common.result.Result;
import com.atguigu.daijia.model.entity.driver.DriverAccount;
import com.atguigu.daijia.model.entity.driver.DriverAccountDetail;
import com.atguigu.daijia.model.form.driver.TransferForm;
import com.atguigu.daijia.model.vo.base.PageVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;

@FeignClient(value = "service-driver")
public interface DriverAccountFeignClient {

    /**
     * 转账
     * @param transferForm 转账表单
     * @return 是否成功
     */
    @PostMapping("/driver/account/transfer")
    Result<Boolean> transfer(@RequestBody TransferForm transferForm);

    /**
     * 获取账户信息
     * @param driverId 司机ID
     * @return 账户信息
     */
    @GetMapping("/driver/account/info/{driverId}")
    Result<DriverAccount> getAccountInfo(@PathVariable Long driverId);

    /**
     * 获取账户明细列表
     * @param driverId 司机ID
     * @param page 页码
     * @param limit 每页记录数
     * @return 账户明细分页
     */
    @GetMapping("/driver/account/detail/{driverId}/{page}/{limit}")
    Result<PageVo<DriverAccountDetail>> getAccountDetailPage(
            @PathVariable Long driverId,
            @PathVariable Long page,
            @PathVariable Long limit);

    /**
     * 提现
     * @param transferForm 提现表单
     * @return 是否成功
     */
    @PostMapping("/driver/account/withdraw")
    Result<Boolean> withdraw(@RequestBody TransferForm transferForm);

    /**
     * 锁定收入
     * @param driverId 司机ID
     * @param amount 金额
     * @param tradeNo 交易编号
     * @param content 描述
     * @return 是否成功
     */
    @GetMapping("/driver/account/lockIncome/{driverId}/{amount}/{tradeNo}/{content}")
    Result<Boolean> lockIncome(@PathVariable Long driverId,
                               @PathVariable BigDecimal amount,
                               @PathVariable String tradeNo,
                               @PathVariable String content);

    /**
     * 解锁收入
     * @param driverId 司机ID
     * @param amount 金额
     * @param tradeNo 交易编号
     * @param content 描述
     * @return 是否成功
     */
    @GetMapping("/driver/account/unlockIncome/{driverId}/{amount}/{tradeNo}/{content}")
    Result<Boolean> unlockIncome(@PathVariable Long driverId,
                                 @PathVariable BigDecimal amount,
                                 @PathVariable String tradeNo,
                                 @PathVariable String content);
}