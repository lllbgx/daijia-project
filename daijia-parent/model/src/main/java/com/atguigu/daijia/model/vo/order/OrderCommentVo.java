package com.atguigu.daijia.model.vo.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "订单评价信息")
public class OrderCommentVo {

    @Schema(description = "订单ID")
    private Long orderId;

    @Schema(description = "司机ID")
    private Long driverId;

    @Schema(description = "顾客ID")
    private Long customerId;

    @Schema(description = "评分，1星~5星")
    private Integer rate;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "状态，1未申诉，2已申诉，3申诉失败，4申诉成功")
    private Integer status;

    @Schema(description = "申诉工作流ID")
    private String instanceId;
}