package cc.geektip.geekoj.judgeservice.judge.codesandbox;

import cc.geektip.geekoj.api.codesandbox.dto.ExecuteCodeRequest;
import cc.geektip.geekoj.api.codesandbox.dto.ExecuteCodeResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * @description: 代码沙箱代理
 * @author: Fish
 * @date: 2024/2/28
 */
@Slf4j
public class CodeSandboxProxy implements CodeSandbox {

    private final CodeSandbox codeSandbox;


    public CodeSandboxProxy(CodeSandbox codeSandbox) {
        this.codeSandbox = codeSandbox;
    }

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        log.info("代码沙箱请求信息：" + executeCodeRequest.toString());
        ExecuteCodeResponse executeCodeResponse = codeSandbox.executeCode(executeCodeRequest);
        log.info("代码沙箱响应信息：" + executeCodeResponse.toString());
        return executeCodeResponse;
    }
}
