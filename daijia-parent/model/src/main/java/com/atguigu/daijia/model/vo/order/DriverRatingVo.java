package com.atguigu.daijia.model.vo.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "司机评分统计")
public class DriverRatingVo {

    @Schema(description = "评价总数")
    private Integer totalCount;

    @Schema(description = "平均评分")
    private Double avgScore;

    @Schema(description = "5星评价数量")
    private Integer fiveStarCount;

    public DriverRatingVo(Integer totalCount, Double avgScore, Integer fiveStarCount) {
        this.totalCount = totalCount;
        this.avgScore = avgScore;
        this.fiveStarCount = fiveStarCount;
    }
}