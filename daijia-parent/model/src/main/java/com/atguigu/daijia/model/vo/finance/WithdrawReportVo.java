package com.atguigu.daijia.model.vo.finance;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "提现记录VO")
public class WithdrawReportVo {

    @Schema(description = "记录ID")
    private Long id;

    @Schema(description = "交易号")
    private String tradeNo;

    @Schema(description = "司机ID")
    private Long driverId;

    @Schema(description = "司机姓名")
    private String driverName;

    @Schema(description = "司机手机")
    private String driverPhone;

    @Schema(description = "交易类型")
    private String tradeTypeName;

    @Schema(description = "金额")
    private BigDecimal amount;

    @Schema(description = "账户余额（可用金额）")
    private BigDecimal availableAmount;

    @Schema(description = "交易说明")
    private String content;

    @Schema(description = "交易时间")
    private String createTime;
}