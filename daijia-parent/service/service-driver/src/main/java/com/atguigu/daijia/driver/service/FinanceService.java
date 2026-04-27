package com.atguigu.daijia.driver.service;

import com.atguigu.daijia.model.form.order.OrderQueryForm;
import com.atguigu.daijia.model.form.finance.WithdrawReportQueryForm;
import com.atguigu.daijia.model.vo.base.PageVo;
import com.atguigu.daijia.model.vo.finance.FinanceStatisticsVo;
import com.atguigu.daijia.model.vo.finance.OrderReportVo;
import com.atguigu.daijia.model.vo.finance.WithdrawReportVo;

public interface FinanceService {

    /**
     * 获取财务统计数据
     * @return 财务统计VO
     */
    FinanceStatisticsVo getStatistics();

    /**
     * 获取订单报表列表（分页）
     * @param page 页码
     * @param limit 每页记录数
     * @param queryForm 查询条件
     * @return 订单报表分页
     */
    PageVo<OrderReportVo> getOrderReportPage(Long page, Long limit, OrderQueryForm queryForm);

    /**
     * 获取提现记录列表（分页）
     * @param page 页码
     * @param limit 每页记录数
     * @param form 查询条件
     * @return 提现记录分页
     */
    PageVo<WithdrawReportVo> getWithdrawReportPage(Long page, Long limit, WithdrawReportQueryForm form);
}