package cc.geektip.geekoj.api.model.enums.question;

import cn.hutool.core.util.StrUtil;
import lombok.Getter;
import org.apache.commons.lang3.ObjectUtils;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * @author Fish
 *
 * @description 用户题目状态枚举
 */
@Getter
public enum UserQuestionStatusEnum implements Serializable {

    // 0 - 全部、1 - 未开始、2 - 尝试过、3 - 已通过
    ALL("全部", 0),
    NOT_STARTED("未开始", 1),
    ATTEMPTED("尝试过", 2),
    PASSED("已通过", 3);

    private final String text;
    private final Integer value;

    UserQuestionStatusEnum(String text, Integer value) {
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
    public static UserQuestionStatusEnum getEnumByValue(Integer value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (UserQuestionStatusEnum anEnum : UserQuestionStatusEnum.values()) {
            if (anEnum.getValue().equals(value)) {
                return anEnum;
            }
        }
        return null;
    }
    /**
     * 根据 text 获取枚举
     *
     * @param text
     * @return
     */
    public static UserQuestionStatusEnum getEnumByText(String text) {
        if (StrUtil.isBlank(text)) {
            return null;
        }
        for (UserQuestionStatusEnum anEnum : UserQuestionStatusEnum.values()) {
            if (anEnum.getText().equals(text)) {
                return anEnum;
            }
        }
        return null;
    }

}
