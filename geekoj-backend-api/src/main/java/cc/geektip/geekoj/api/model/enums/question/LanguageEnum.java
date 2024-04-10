package cc.geektip.geekoj.api.model.enums.question;

import lombok.Getter;
import org.apache.commons.lang3.ObjectUtils;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * @author Bill Yu
 * @description 题目提交编程语言枚举
 */
@Getter
public enum LanguageEnum implements Serializable {

    JAVA("Java", "java"),
    CPLUSPLUS("C++", "cpp"),
    GOLANG("Golang", "go");

    private final String text;

    private final String value;

    LanguageEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 获取值列表
     *
     * @return
     */
    public static List<String> getValues() {
        return Arrays.stream(values()).map(item -> item.value).toList();
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value
     * @return
     */
    public static LanguageEnum getEnumByValue(String value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (LanguageEnum anEnum : LanguageEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }

}
