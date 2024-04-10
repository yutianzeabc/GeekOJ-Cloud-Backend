package cc.geektip.geekoj.api.model.enums.question;

import lombok.Getter;
import org.apache.commons.lang3.ObjectUtils;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * @author Bill Yu
 *
 * @description 题目提交枚举
 */
@Getter
public enum QuestionSubmitStatusEnum implements Serializable {

    // 0 - 等待中、1 - 判题中、2 - 解答错误、3 - 通过
    WAITING("等待中", 0),
    RUNNING("判题中", 1),
    FAILED("解答错误", 2),
    SUCCEED("通过", 3);

    private final String text;
    private final Integer value;

    QuestionSubmitStatusEnum(String text, Integer value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 获取值列表
     *
     * @return
     */
    public static List<Integer> getValues() {
        return Arrays.stream(values()).map(item -> item.value).toList();
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value
     * @return
     */
    public static QuestionSubmitStatusEnum getEnumByValue(Integer value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (QuestionSubmitStatusEnum anEnum : QuestionSubmitStatusEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }
}
