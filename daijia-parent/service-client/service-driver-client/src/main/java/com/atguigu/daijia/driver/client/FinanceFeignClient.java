package com.atguigu.daijia.driver.client;

import com.atguigu.daijia.common.result.Result;
import com.atguigu.daijia.model.form.order.OrderQueryForm;
import com.atguigu.daijia.model.form.finance.WithdrawReportQueryForm;
import com.atguigu.daijia.model.vo.base.PageVo;
import com.atguigu.daijia.model.vo.finance.FinanceStatisticsVo;
import com.atguigu.daijia.model.vo.finance.OrderReportVo;
import com.atguigu.daijia.model.vo.finance.WithdrawReportVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "service-driver")
public interface FinanceFeignClient {

    /**
     * 获取财务统计数据
     * @return 财务统计VO
     */
    @GetMapping("/driver/finance/statistics")
    Result<FinanceStatisticsVo> getStatistics();

    /**
     * 获取订单报表列表（分页）
     * @param page 页码
     * @param limit 每页记录数
     * @param queryForm 查询条件
     * @return 订单报表分页
     */
    @PostMapping("/driver/finance/order/report/{page}/{limit}")
    Result<PageVo<OrderReportVo>> getOrderReportPage(
            @PathVariable Long page,
            @PathVariable Long limit,
            @RequestBody OrderQueryForm queryForm);

    /**
     * 获取提现记录列表（分页）
     * @param page 页码
     * @param limit 每页记录数
     * @param form 查询条件
     * @return 提现记录分页
     */
    @PostMapping("/driver/finance/withdraw/report/{page}/{limit}")
    Result<PageVo<WithdrawReportVo>> getWithdrawReportPage(
            @PathVariable Long page,
            @PathVariable Long limit,
            @RequestBody WithdrawReportQueryForm form);
}