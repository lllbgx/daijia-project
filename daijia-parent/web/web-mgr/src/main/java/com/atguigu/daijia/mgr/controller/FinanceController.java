package com.atguigu.daijia.mgr.controller;

import com.atguigu.daijia.common.result.Result;
import com.atguigu.daijia.driver.client.FinanceFeignClient;
import com.atguigu.daijia.model.form.order.OrderQueryForm;
import com.atguigu.daijia.model.form.finance.WithdrawReportQueryForm;
import com.atguigu.daijia.model.vo.base.PageVo;
import com.atguigu.daijia.model.vo.finance.FinanceStatisticsVo;
import com.atguigu.daijia.model.vo.finance.OrderReportVo;
import com.atguigu.daijia.model.vo.finance.WithdrawReportVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "财务管理接口")
@RestController
@RequestMapping("/mgr/finance")
public class FinanceController {

    @Autowired
    private FinanceFeignClient financeFeignClient;

    @Operation(summary = "获取财务统计数据")
    @GetMapping("/statistics")
    public Result<FinanceStatisticsVo> getStatistics() {
        return financeFeignClient.getStatistics();
    }

    @Operation(summary = "获取订单报表列表（分页）")
    @PostMapping("/order/report/{page}/{limit}")
    public Result<PageVo<OrderReportVo>> getOrderReportPage(
            @Parameter(name = "page", description = "当前页码", required = true)
            @PathVariable Long page,
            @Parameter(name = "limit", description = "每页记录数", required = true)
            @PathVariable Long limit,
            @RequestBody OrderQueryForm queryForm) {
        return financeFeignClient.getOrderReportPage(page, limit, queryForm);
    }

    @Operation(summary = "获取提现记录列表（分页）")
    @PostMapping("/withdraw/report/{page}/{limit}")
    public Result<PageVo<WithdrawReportVo>> getWithdrawReportPage(
            @Parameter(name = "page", description = "当前页码", required = true)
            @PathVariable Long page,
            @Parameter(name = "limit", description = "每页记录数", required = true)
            @PathVariable Long limit,
            @RequestBody WithdrawReportQueryForm form) {
        return financeFeignClient.getWithdrawReportPage(page, limit, form);
    }
}