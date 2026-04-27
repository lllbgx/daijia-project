package com.atguigu.daijia.model.form.finance;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "提现记录查询表单")
public class WithdrawReportQueryForm implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "开始日期")
    private String startDate;

    @Schema(description = "结束日期")
    private String endDate;

    @Schema(description = "司机ID")
    private Long driverId;

    @Schema(description = "交易号")
    private String tradeNo;
}