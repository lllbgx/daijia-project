package com.atguigu.daijia.model.form.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "订单查询表单")
public class OrderQueryForm {

    @Schema(description = "订单号")
    private String orderNo;

    @Schema(description = "乘客ID")
    private Long customerId;

    @Schema(description = "司机ID")
    private Long driverId;

    @Schema(description = "订单状态")
    private Integer status;
}
