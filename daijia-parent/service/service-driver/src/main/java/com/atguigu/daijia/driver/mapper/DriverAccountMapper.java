package com.atguigu.daijia.driver.mapper;

import com.atguigu.daijia.model.entity.driver.DriverAccount;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

@Mapper
public interface DriverAccountMapper extends BaseMapper<DriverAccount> {

    // 添加奖励到司机账户表
    void add(@Param("driverId") Long driverId, @Param("amount") BigDecimal amount);

    // 锁定收入：总金额↑ 锁定金额↑
    void lockIncome(@Param("driverId") Long driverId, @Param("amount") BigDecimal amount);

    // 解锁收入：锁定金额↓ 可用金额↑
    void unlockIncome(@Param("driverId") Long driverId, @Param("amount") BigDecimal amount);

    // 提现：可用金额↓ 总支出↑
    void withdraw(@Param("driverId") Long driverId, @Param("amount") BigDecimal amount);
}
