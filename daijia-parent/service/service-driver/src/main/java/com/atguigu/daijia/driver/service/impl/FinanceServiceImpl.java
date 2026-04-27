package com.atguigu.daijia.driver.service.impl;

import com.atguigu.daijia.driver.mapper.DriverAccountDetailMapper;
import com.atguigu.daijia.driver.mapper.DriverAccountMapper;
import com.atguigu.daijia.driver.mapper.DriverInfoMapper;
import com.atguigu.daijia.driver.service.FinanceService;
import com.atguigu.daijia.model.entity.driver.DriverAccount;
import com.atguigu.daijia.model.entity.driver.DriverAccountDetail;
import com.atguigu.daijia.model.entity.driver.DriverInfo;
import com.atguigu.daijia.model.entity.order.OrderInfo;
import com.atguigu.daijia.model.form.order.OrderQueryForm;
import com.atguigu.daijia.model.form.finance.WithdrawReportQueryForm;
import com.atguigu.daijia.model.vo.base.PageVo;
import com.atguigu.daijia.model.vo.finance.FinanceStatisticsVo;
import com.atguigu.daijia.model.vo.finance.OrderReportVo;
import com.atguigu.daijia.model.vo.finance.WithdrawReportVo;
import com.atguigu.daijia.model.vo.order.OrderBillVo;
import com.atguigu.daijia.model.vo.order.OrderProfitsharingVo;
import com.atguigu.daijia.order.client.OrderInfoFeignClient;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@SuppressWarnings({"unchecked", "rawtypes"})
public class FinanceServiceImpl extends ServiceImpl<DriverAccountDetailMapper, DriverAccountDetail> implements FinanceService {

    @Autowired
    private DriverAccountDetailMapper driverAccountDetailMapper;

    @Autowired
    private DriverAccountMapper driverAccountMapper;

    @Autowired
    private DriverInfoMapper driverInfoMapper;

    @Autowired
    private OrderInfoFeignClient orderInfoFeignClient;

    @Override
    public FinanceStatisticsVo getStatistics() {
        FinanceStatisticsVo vo = new FinanceStatisticsVo();

        // 获取今日开始和结束时间
        String todayStart = getTodayStart();
        String todayEnd = getTodayEnd();
        String weekStart = getWeekStart();
        String monthStart = getMonthStart();

        // 1. 查询订单相关统计数据（通过Feign调用service-order）
        queryOrderStatistics(vo, todayStart, todayEnd, weekStart, monthStart);

        // 2. 查询提现统计数据（本地查询DriverAccountDetail）
        queryWithdrawStatistics(vo, todayStart, todayEnd, weekStart, monthStart);

        return vo;
    }

    private void queryOrderStatistics(FinanceStatisticsVo vo, String todayStart, String todayEnd, String weekStart, String monthStart) {
        try {
            // 通过Feign调用获取今日订单数
            Long todayOrderCount = orderInfoFeignClient.getOrderNumByTime(todayStart, todayEnd).getData();
            vo.setTodayOrderCount(todayOrderCount != null ? todayOrderCount : 0L);

            // 本周订单数
            Long weekOrderCount = orderInfoFeignClient.getOrderNumByTime(weekStart, todayEnd).getData();
            vo.setWeekOrderCount(weekOrderCount != null ? weekOrderCount : 0L);

            // 本月订单数
            Long monthOrderCount = orderInfoFeignClient.getOrderNumByTime(monthStart, todayEnd).getData();
            vo.setMonthOrderCount(monthOrderCount != null ? monthOrderCount : 0L);

            // 营收数据
            BigDecimal todayIncome = orderInfoFeignClient.getIncomeByTime(todayStart, todayEnd).getData();
            vo.setTodayIncome(todayIncome != null ? todayIncome : new BigDecimal("0.00"));

            BigDecimal weekIncome = orderInfoFeignClient.getIncomeByTime(weekStart, todayEnd).getData();
            vo.setWeekIncome(weekIncome != null ? weekIncome : new BigDecimal("0.00"));

            BigDecimal monthIncome = orderInfoFeignClient.getIncomeByTime(monthStart, todayEnd).getData();
            vo.setMonthIncome(monthIncome != null ? monthIncome : new BigDecimal("0.00"));

        } catch (Exception e) {
            log.error("查询订单统计数据失败", e);
            vo.setTodayOrderCount(0L);
            vo.setWeekOrderCount(0L);
            vo.setMonthOrderCount(0L);
            vo.setTodayIncome(new BigDecimal("0.00"));
            vo.setWeekIncome(new BigDecimal("0.00"));
            vo.setMonthIncome(new BigDecimal("0.00"));
        }
    }

    private void queryWithdrawStatistics(FinanceStatisticsVo vo, String todayStart, String todayEnd, String weekStart, String monthStart) {
        // 今日提现
        BigDecimal todayWithdraw = getWithdrawAmount(todayStart, todayEnd);
        vo.setTodayWithdraw(todayWithdraw);

        // 本周提现
        BigDecimal weekWithdraw = getWithdrawAmount(weekStart, todayEnd);
        vo.setWeekWithdraw(weekWithdraw);

        // 本月提现
        BigDecimal monthWithdraw = getWithdrawAmount(monthStart, todayEnd);
        vo.setMonthWithdraw(monthWithdraw);
    }

    private BigDecimal getWithdrawAmount(String startTime, String endTime) {
        LambdaQueryWrapper<DriverAccountDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DriverAccountDetail::getTradeType, "1203"); // 提现
        if (StringUtils.hasText(startTime)) {
            wrapper.ge(DriverAccountDetail::getCreateTime, startTime);
        }
        if (StringUtils.hasText(endTime)) {
            wrapper.le(DriverAccountDetail::getCreateTime, endTime);
        }
        List<DriverAccountDetail> list = driverAccountDetailMapper.selectList(wrapper);
        if (list == null || list.isEmpty()) {
            return new BigDecimal("0.00");
        }
        return list.stream()
                .map(DriverAccountDetail::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public PageVo<OrderReportVo> getOrderReportPage(Long page, Long limit, OrderQueryForm queryForm) {
        try {
            // 财务管理只查询有账单的订单（status >= 7，待支付和已支付）
            if (queryForm == null) {
                queryForm = new OrderQueryForm();
            }
            // 只有当前端没有传入status时，才默认使用statusGte >= 7
            // 如果前端明确传了status，就按前端传的精确查询
            if (queryForm.getStatus() == null) {
                queryForm.setStatusGte(7);
            }

            // 调用Feign获取订单分页数据
            com.atguigu.daijia.model.vo.base.PageVo<OrderInfo> orderPage =
                    orderInfoFeignClient.findOrderInfoPageByCondition(page, limit, queryForm).getData();

            if (orderPage == null || orderPage.getRecords() == null || orderPage.getRecords().isEmpty()) {
                PageVo<OrderReportVo> pageVo = new PageVo<>();
                pageVo.setRecords(new ArrayList<>());
                pageVo.setTotal(0L);
                pageVo.setPages(0L);
                return pageVo;
            }

            List<OrderReportVo> reportList = new ArrayList<>();
            for (OrderInfo order : orderPage.getRecords()) {
                OrderReportVo vo = new OrderReportVo();
                vo.setId(order.getId());
                vo.setOrderNo(order.getOrderNo());
                vo.setCustomerId(order.getCustomerId());
                vo.setDriverId(order.getDriverId());
                vo.setStartLocation(order.getStartLocation());
                vo.setEndLocation(order.getEndLocation());
                vo.setStatus(order.getStatus());
                vo.setCreateTime(order.getCreateTime() != null ? order.getCreateTime().toString() : "");

                // 获取订单状态名称
                vo.setStatusName(getStatusName(order.getStatus()));

                // 获取账单信息
                try {
                    OrderBillVo billVo = orderInfoFeignClient.getOrderBillInfo(order.getId()).getData();
                    if (billVo != null) {
                        vo.setPayAmount(billVo.getPayAmount());
                        vo.setDistanceFee(billVo.getDistanceFee());
                        vo.setWaitFee(billVo.getWaitFee());
                        vo.setTollFee(billVo.getTollFee());
                        vo.setParkingFee(billVo.getParkingFee());
                        vo.setOtherFee(billVo.getOtherFee());
                        vo.setCouponAmount(billVo.getCouponAmount());
                    }
                } catch (Exception e) {
                    log.error("获取账单信息失败, orderId={}", order.getId(), e);
                }

                // 获取分账信息
                try {
                    OrderProfitsharingVo profitsharingVo = orderInfoFeignClient.getOrderProfitsharing(order.getId()).getData();
                    if (profitsharingVo != null) {
                        vo.setPlatformIncome(profitsharingVo.getPlatformIncome());
                        vo.setDriverIncome(profitsharingVo.getDriverIncome());
                    }
                } catch (Exception e) {
                    log.error("获取分账信息失败, orderId={}", order.getId(), e);
                }

                reportList.add(vo);
            }

            PageVo<OrderReportVo> pageVo = new PageVo<>();
            pageVo.setRecords(reportList);
            pageVo.setTotal(orderPage.getTotal());
            pageVo.setPages(orderPage.getPages());

            return pageVo;

        } catch (Exception e) {
            log.error("查询订单报表失败", e);
            PageVo<OrderReportVo> pageVo = new PageVo<>();
            pageVo.setRecords(new ArrayList<>());
            pageVo.setTotal(0L);
            pageVo.setPages(0L);
            return pageVo;
        }
    }

    private String getStatusName(Integer status) {
        if (status == null) return "未知";
        switch (status) {
            case 1: return "待接单";
            case 2: return "已接单";
            case 3: return "司机已到达";
            case 5: return "服务中";
            case 7: return "待支付";
            case 8: return "已支付";
            case -1: return "已取消";
            default: return "未知";
        }
    }

    @Override
    public PageVo<WithdrawReportVo> getWithdrawReportPage(Long page, Long limit, WithdrawReportQueryForm form) {
        // 查询提现记录
        Page<DriverAccountDetail> pageParam = new Page<>(page, limit);

        LambdaQueryWrapper<DriverAccountDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DriverAccountDetail::getTradeType, "1203"); // 提现

        // 提取为final变量，避免lambda捕获问题
        final String tradeNo = form.getTradeNo();
        final Long driverId = form.getDriverId();
        final String startDate = form.getStartDate();
        final String endDate = form.getEndDate();
        final String endDateFull = (endDate != null && StringUtils.hasText(endDate)) ? endDate + " 23:59:59" : null;

        if (StringUtils.hasText(tradeNo)) {
            wrapper.eq(DriverAccountDetail::getTradeNo, tradeNo);
        }
        if (driverId != null) {
            wrapper.eq(DriverAccountDetail::getDriverId, driverId);
        }
        if (StringUtils.hasText(startDate)) {
            wrapper.ge(DriverAccountDetail::getCreateTime, startDate);
        }
        if (endDateFull != null) {
            wrapper.le(DriverAccountDetail::getCreateTime, endDateFull);
        }
        wrapper.orderByDesc(DriverAccountDetail::getCreateTime);

        Page<DriverAccountDetail> detailPage = driverAccountDetailMapper.selectPage(pageParam, wrapper);

        // 获取所有涉及的司机ID
        List<Long> driverIds = detailPage.getRecords().stream()
                .map(DriverAccountDetail::getDriverId)
                .distinct()
                .collect(Collectors.toList());

        // ===================== 修复位置 =====================
        // 批量查询司机信息（只赋值一次，满足 effectively final）
        Map<Long, DriverInfo> driverInfoMap;
        if (!driverIds.isEmpty()) {
            List<DriverInfo> driverInfos = driverInfoMapper.selectBatchIds(driverIds);
            driverInfoMap = driverInfos.stream()
                    .collect(Collectors.toMap(DriverInfo::getId, d -> d));
        } else {
            driverInfoMap = new HashMap<>();
        }

        // 批量查询司机账户余额（只赋值一次，满足 effectively final）
        Map<Long, DriverAccount> driverAccountMap;
        if (!driverIds.isEmpty()) {
            List<DriverAccount> accounts = driverAccountMapper.selectBatchIds(driverIds);
            driverAccountMap = accounts.stream()
                    .collect(Collectors.toMap(DriverAccount::getId, a -> a));
        } else {
            driverAccountMap = new HashMap<>();
        }
        // ======================================================

        // 转换结果
        List<WithdrawReportVo> records = detailPage.getRecords().stream()
                .map(detail -> {
                    WithdrawReportVo vo = new WithdrawReportVo();
                    vo.setId(detail.getId());
                    vo.setTradeNo(detail.getTradeNo());
                    vo.setDriverId(detail.getDriverId());
                    vo.setAmount(detail.getAmount());
                    vo.setContent(detail.getContent());
                    vo.setCreateTime(detail.getCreateTime() != null ? detail.getCreateTime().toString() : "");
                    vo.setTradeTypeName("提现");

                    DriverInfo driverInfo = driverInfoMap.get(detail.getDriverId());
                    if (driverInfo != null) {
                        vo.setDriverName(driverInfo.getName());
                        vo.setDriverPhone(driverInfo.getPhone());
                    }

                    DriverAccount account = driverAccountMap.get(detail.getDriverId());
                    if (account != null) {
                        vo.setAvailableAmount(account.getAvailableAmount());
                    }

                    return vo;
                })
                .collect(Collectors.toList());

        PageVo<WithdrawReportVo> pageVo = new PageVo<>();
        pageVo.setRecords(records);
        pageVo.setTotal(detailPage.getTotal());
        pageVo.setPages(detailPage.getPages());

        return pageVo;
    }

    private String getTodayStart() {
        return LocalDate.now().toString() + " 00:00:00";
    }

    private String getTodayEnd() {
        return LocalDate.now().toString() + " 23:59:59";
    }

    private String getWeekStart() {
        return LocalDate.now().minusWeeks(1).toString() + " 00:00:00";
    }

    private String getMonthStart() {
        return LocalDate.now().withDayOfMonth(1).toString() + " 00:00:00";
    }
}