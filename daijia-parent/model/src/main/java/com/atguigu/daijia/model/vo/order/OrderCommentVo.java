package com.atguigu.daijia.model.vo.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

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

    @Schema(description = "创建时间")
    private Date createTime;
}