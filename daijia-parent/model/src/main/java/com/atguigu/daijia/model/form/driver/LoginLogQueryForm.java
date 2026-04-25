package com.atguigu.daijia.model.form.driver;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "司机登录日志查询表单")
public class LoginLogQueryForm {

    @Schema(description = "司机ID")
    private Long driverId;

    @Schema(description = "IP地址")
    private String ipaddr;
}
