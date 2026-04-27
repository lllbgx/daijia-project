package com.atguigu.daijia.model.form.finance;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@Schema(description = "订单报表查询表单")
public class OrderReportQueryForm implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "开始日期")
    private String startDate;

    @Schema(description = "结束日期")
    private String endDate;

    @Schema(description = "订单号")
    private String orderNo;

    @Schema(description = "订单状态: 8-已支付")
    private Integer status;
}