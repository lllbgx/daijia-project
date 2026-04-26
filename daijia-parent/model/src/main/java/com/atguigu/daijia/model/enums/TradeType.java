package com.atguigu.daijia.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

@Getter
public enum TradeType {

    INCOME(1201, "进账"),
    UNLOCK(1202, "解锁"),
    WITHDRAW(1203, "提现"),
    REWARD(1204, "系统奖励"),
    ;

    @EnumValue
    private Integer type;
    private String content;

    TradeType(Integer type, String content) {
        this.type = type;
        this.content = content;
    }

}
