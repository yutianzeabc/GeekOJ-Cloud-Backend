package cc.geektip.geekoj.judgeservice.judge.strategy;

import lombok.Getter;

/**
 * @description: 判题策略枚举类
 * @author: Fish
 *
 */
@Getter
public enum JudgeStrategyEnum {
    DEFAULT("default"),
    JAVA("java"),
    GO("go"),
    CPP("cpp");

    final String value;

    JudgeStrategyEnum(String value) {
        this.value = value;
    }
}
