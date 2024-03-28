package cc.geektip.geekoj.judgeservice.judge.codesandbox.impl;

import cc.geektip.geekoj.api.codesandbox.dto.ExecuteCodeRequest;
import cc.geektip.geekoj.api.codesandbox.dto.ExecuteCodeResponse;
import cc.geektip.geekoj.judgeservice.judge.codesandbox.CodeSandbox;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @description: 第三方代码沙箱
 * @author: Fish
 * @date: 2024/2/28
 */
@Slf4j
@Component
public class ThirdPartyCodeSandbox implements CodeSandbox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest request) {
        System.out.println("第三方代码沙箱");
        return null;
    }
}
