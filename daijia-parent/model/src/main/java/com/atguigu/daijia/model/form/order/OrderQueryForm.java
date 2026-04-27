package com.atguigu.daijia.model.form.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "订单查询表单")
public class OrderQueryForm implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "订单号")
    private String orderNo;

    @Schema(description = "乘客ID")
    private Long customerId;

    @Schema(description = "司机ID")
    private Long driverId;

    @Schema(description = "订单状态")
    private Integer status;

    @Schema(description = "订单状态（大于等于，用于财务统计）")
    private Integer statusGte;

    @Schema(description = "开始日期")
    private String startDate;

    @Schema(description = "结束日期")
    private String endDate;
}
