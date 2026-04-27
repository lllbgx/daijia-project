package com.atguigu.daijia.model.vo.finance;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "订单报表VO")
public class OrderReportVo {

    @Schema(description = "订单ID")
    private Long id;

    @Schema(description = "订单号")
    private String orderNo;

    @Schema(description = "乘客ID")
    private Long customerId;

    @Schema(description = "司机ID")
    private Long driverId;

    @Schema(description = "上车地点")
    private String startLocation;

    @Schema(description = "下车地点")
    private String endLocation;

    @Schema(description = "订单金额（实付金额）")
    private BigDecimal payAmount;

    @Schema(description = "里程费")
    private BigDecimal distanceFee;

    @Schema(description = "等时费")
    private BigDecimal waitFee;

    @Schema(description = "路桥费")
    private BigDecimal tollFee;

    @Schema(description = "停车费")
    private BigDecimal parkingFee;

    @Schema(description = "其他费用")
    private BigDecimal otherFee;

    @Schema(description = "优惠券金额")
    private BigDecimal couponAmount;

    @Schema(description = "平台收入")
    private BigDecimal platformIncome;

    @Schema(description = "司机收入")
    private BigDecimal driverIncome;

    @Schema(description = "订单状态")
    private Integer status;

    @Schema(description = "订单状态名称")
    private String statusName;

    @Schema(description = "支付时间")
    private String payTime;

    @Schema(description = "创建时间")
    private String createTime;
}