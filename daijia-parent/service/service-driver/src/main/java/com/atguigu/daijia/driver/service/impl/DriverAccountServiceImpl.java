package com.atguigu.daijia.driver.service.impl;

import com.atguigu.daijia.driver.mapper.DriverAccountDetailMapper;
import com.atguigu.daijia.driver.mapper.DriverAccountMapper;
import com.atguigu.daijia.driver.service.DriverAccountService;
import com.atguigu.daijia.model.entity.driver.DriverAccount;
import com.atguigu.daijia.model.entity.driver.DriverAccountDetail;
import com.atguigu.daijia.model.form.driver.TransferForm;
import com.atguigu.daijia.model.vo.base.PageVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

@Slf4j
@Service
@SuppressWarnings({"unchecked", "rawtypes"})
public class DriverAccountServiceImpl extends ServiceImpl<DriverAccountMapper, DriverAccount> implements DriverAccountService {

    @Autowired
    private DriverAccountMapper driverAccountMapper;

    @Autowired
    private DriverAccountDetailMapper driverAccountDetailMapper;

    @Override
    public Boolean transfer(TransferForm transferForm) {
        //1 去重
        LambdaQueryWrapper<DriverAccountDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DriverAccountDetail::getTradeNo, transferForm.getTradeNo());
        Long count = driverAccountDetailMapper.selectCount(wrapper);
        if (count > 0) {
            return true;
        }

        //2 添加奖励到司机账户表
        driverAccountMapper.add(transferForm.getDriverId(), transferForm.getAmount());

        //3 添加交易记录
        DriverAccountDetail driverAccountDetail = new DriverAccountDetail();
        driverAccountDetail.setDriverId(transferForm.getDriverId());
        driverAccountDetail.setContent(transferForm.getContent());
        driverAccountDetail.setTradeType(String.valueOf(transferForm.getTradeType()));
        driverAccountDetail.setAmount(transferForm.getAmount());
        driverAccountDetail.setTradeNo(transferForm.getTradeNo());
        driverAccountDetailMapper.insert(driverAccountDetail);

        return true;
    }

    @Override
    @Transactional
    public Boolean lockIncome(Long driverId, BigDecimal amount, String tradeNo, String content) {
        // 1. 去重检查
        LambdaQueryWrapper<DriverAccountDetail> detailWrapper = new LambdaQueryWrapper<>();
        detailWrapper.eq(DriverAccountDetail::getTradeNo, tradeNo);
        Long count = driverAccountDetailMapper.selectCount(detailWrapper);
        if (count > 0) {
            return true;
        }

        // 2. 记录明细（1201-进账）
        DriverAccountDetail detail = new DriverAccountDetail();
        detail.setDriverId(driverId);
        detail.setContent(content);
        detail.setTradeType("1201");
        detail.setAmount(amount);
        detail.setTradeNo(tradeNo);
        detail.setCreateTime(new Date());
        detail.setUpdateTime(new Date());
        driverAccountDetailMapper.insert(detail);

        // 3. 更新账户：总金额↑ 锁定金额↑
        driverAccountMapper.lockIncome(driverId, amount);

        return true;
    }

    @Override
    @Transactional
    public Boolean unlockIncome(Long driverId, BigDecimal amount, String tradeNo, String content) {
        // 1. 去重检查
        LambdaQueryWrapper<DriverAccountDetail> detailWrapper = new LambdaQueryWrapper<>();
        detailWrapper.eq(DriverAccountDetail::getTradeNo, tradeNo);
        Long count = driverAccountDetailMapper.selectCount(detailWrapper);
        if (count > 0) {
            return true;
        }

        // 2. 记录明细（1202-解锁）
        DriverAccountDetail detail = new DriverAccountDetail();
        detail.setDriverId(driverId);
        detail.setContent(content);
        detail.setTradeType("1202");
        detail.setAmount(amount);
        detail.setTradeNo(tradeNo);
        detail.setCreateTime(new Date());
        detail.setUpdateTime(new Date());
        driverAccountDetailMapper.insert(detail);

        // 3. 更新账户：锁定金额↓ 可用金额↑
        driverAccountMapper.unlockIncome(driverId, amount);

        return true;
    }

    @Override
    @Transactional
    public Boolean withdraw(Long driverId, BigDecimal amount, String tradeNo, String content) {
        // 1. 去重检查
        LambdaQueryWrapper<DriverAccountDetail> detailWrapper = new LambdaQueryWrapper<>();
        detailWrapper.eq(DriverAccountDetail::getTradeNo, tradeNo);
        Long count = driverAccountDetailMapper.selectCount(detailWrapper);
        if (count > 0) {
            return true;
        }

        // 2. 记录明细（1203-提现）
        DriverAccountDetail detail = new DriverAccountDetail();
        detail.setDriverId(driverId);
        detail.setContent(content);
        detail.setTradeType("1203");
        detail.setAmount(amount);
        detail.setTradeNo(tradeNo);
        detail.setCreateTime(new Date());
        detail.setUpdateTime(new Date());
        driverAccountDetailMapper.insert(detail);

        // 3. 更新账户：可用金额↓ 总支出↑
        driverAccountMapper.withdraw(driverId, amount);

        return true;
    }

    @Override
    public DriverAccount getByDriverId(Long driverId) {
        LambdaQueryWrapper<DriverAccount> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DriverAccount::getDriverId, driverId);
        return driverAccountMapper.selectOne(wrapper);
    }

    @Override
    public PageVo<DriverAccountDetail> getDetailPage(Long driverId, Long page, Long limit) {
        LambdaQueryWrapper<DriverAccountDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DriverAccountDetail::getDriverId, driverId);
        wrapper.orderByDesc(DriverAccountDetail::getCreateTime);
        Page<DriverAccountDetail> pageParam = new Page<>(page, limit);
        IPage<DriverAccountDetail> pageResult = driverAccountDetailMapper.selectPage(pageParam, wrapper);
        return new PageVo<>(pageResult.getRecords(), pageResult.getPages(), pageResult.getTotal());
    }
}
