package com.atguigu.daijia.model.form.driver;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "司机查询表单")
public class DriverQueryForm {

    @Schema(description = "司机姓名")
    private String name;

    @Schema(description = "认证状态")
    private Integer authStatus;

    @Schema(description = "司机状态")
    private Integer status;
}
