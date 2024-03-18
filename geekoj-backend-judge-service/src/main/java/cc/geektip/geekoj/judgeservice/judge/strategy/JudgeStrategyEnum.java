package cc.geektip.geekoj.judgeservice.judge.strategy;

import lombok.Getter;

/**
 * @description: 判题策略枚举类
 * @author: Fish
 * @date: 2024/2/29
 */
@Getter
public enum JudgeStrategyEnum {
    DEFAULT("default"),
    JAVA("java"),
    CPP("cpp");

    final String value;

    JudgeStrategyEnum(String value) {
        this.value = value;
    }
}
