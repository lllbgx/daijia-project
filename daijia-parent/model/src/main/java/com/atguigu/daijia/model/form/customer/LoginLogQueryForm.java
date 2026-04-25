package com.atguigu.daijia.model.form.customer;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "用户登录日志查询表单")
public class LoginLogQueryForm {

    @Schema(description = "用户ID")
    private Long customerId;

    @Schema(description = "IP地址")
    private String ipaddr;
}
