package com.atguigu.daijia.model.vo.finance;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "财务统计VO")
public class FinanceStatisticsVo {

    @Schema(description = "今日营收")
    private BigDecimal todayIncome;

    @Schema(description = "本周营收")
    private BigDecimal weekIncome;

    @Schema(description = "本月营收")
    private BigDecimal monthIncome;

    @Schema(description = "今日订单量")
    private Long todayOrderCount;

    @Schema(description = "本周订单量")
    private Long weekOrderCount;

    @Schema(description = "本月订单量")
    private Long monthOrderCount;

    @Schema(description = "今日提现金额")
    private BigDecimal todayWithdraw;

    @Schema(description = "本周提现金额")
    private BigDecimal weekWithdraw;

    @Schema(description = "本月提现金额")
    private BigDecimal monthWithdraw;
}