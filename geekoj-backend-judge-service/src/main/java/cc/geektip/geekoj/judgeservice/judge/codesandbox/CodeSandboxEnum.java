package cc.geektip.geekoj.judgeservice.judge.codesandbox;

import lombok.Getter;

/**
 * @description: 代码沙箱类型枚举类
 * @author: Bill Yu
 *
 */
@Getter
public enum CodeSandboxEnum {
    EXAMPLE("example"),
    REMOTE("remote"),
    THIRD_PARTY("thirdParty");

    final String value;

    CodeSandboxEnum(String value) {
        this.value = value;
    }
}
