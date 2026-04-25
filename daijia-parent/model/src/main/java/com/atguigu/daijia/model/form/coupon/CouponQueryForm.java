package com.atguigu.daijia.model.form.coupon;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "优惠券查询表单")
public class CouponQueryForm {

    @Schema(description = "优惠券名称")
    private String name;

    @Schema(description = "优惠券状态")
    private Integer status;
}
