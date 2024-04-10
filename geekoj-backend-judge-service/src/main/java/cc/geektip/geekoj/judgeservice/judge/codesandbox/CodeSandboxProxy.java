package cc.geektip.geekoj.judgeservice.judge.codesandbox;

import cc.geektip.geekoj.api.model.dto.codesandbox.ExecuteCodeRequest;
import cc.geektip.geekoj.api.model.dto.codesandbox.ExecuteCodeResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * @description: 代码沙箱代理
 * @author: Bill Yu
 *
 */
@Slf4j
public class CodeSandboxProxy implements CodeSandbox {

    private final CodeSandbox codeSandbox;


    public CodeSandboxProxy(CodeSandbox codeSandbox) {
        this.codeSandbox = codeSandbox;
    }

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        log.debug("代码沙箱请求信息：" + executeCodeRequest.toString());
        ExecuteCodeResponse executeCodeResponse = codeSandbox.executeCode(executeCodeRequest);
        log.debug("代码沙箱响应信息：" + executeCodeResponse.toString());
        return executeCodeResponse;
    }
}
