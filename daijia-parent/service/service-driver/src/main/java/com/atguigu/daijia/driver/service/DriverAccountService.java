package com.atguigu.daijia.driver.service;

import com.atguigu.daijia.model.entity.driver.DriverAccount;
import com.atguigu.daijia.model.entity.driver.DriverAccountDetail;
import com.atguigu.daijia.model.form.driver.TransferForm;
import com.atguigu.daijia.model.vo.base.PageVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;

public interface DriverAccountService extends IService<DriverAccount> {

    Boolean transfer(TransferForm transferForm);

    /**
     * 锁定收入（生成账单分账时调用）
     * @param driverId 司机ID
     * @param amount 金额
     * @param tradeNo 交易编号
     * @param content 交易内容
     * @return 是否成功
     */
    Boolean lockIncome(Long driverId, BigDecimal amount, String tradeNo, String content);

    /**
     * 解锁收入（用户支付成功时调用）
     * @param driverId 司机ID
     * @param amount 金额
     * @param tradeNo 交易编号
     * @param content 交易内容
     * @return 是否成功
     */
    Boolean unlockIncome(Long driverId, BigDecimal amount, String tradeNo, String content);

    /**
     * 提现
     * @param driverId 司机ID
     * @param amount 金额
     * @param tradeNo 交易编号
     * @param content 交易内容
     * @return 是否成功
     */
    Boolean withdraw(Long driverId, BigDecimal amount, String tradeNo, String content);

    /**
     * 获取账户信息
     * @param driverId 司机ID
     * @return 账户信息
     */
    DriverAccount getByDriverId(Long driverId);

    /**
     * 获取账户明细列表（分页）
     * @param driverId 司机ID
     * @param page 页码
     * @param limit 每页记录数
     * @return 账户明细分页
     */
    PageVo<DriverAccountDetail> getDetailPage(Long driverId, Long page, Long limit);
}
