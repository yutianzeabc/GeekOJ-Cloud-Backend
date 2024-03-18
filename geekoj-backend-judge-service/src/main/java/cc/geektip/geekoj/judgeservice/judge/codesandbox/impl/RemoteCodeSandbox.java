package cc.geektip.geekoj.judgeservice.judge.codesandbox.impl;

import cc.geektip.geekoj.api.model.codesandbox.ExecuteCodeRequest;
import cc.geektip.geekoj.api.model.codesandbox.ExecuteCodeResponse;
import cc.geektip.geekoj.judgeservice.client.RemoteCodeSandboxClient;
import cc.geektip.geekoj.judgeservice.judge.codesandbox.CodeSandbox;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


/**
 * @description: 远程代码沙箱
 * @author: Fish
 * @date: 2024/2/28
 */
@Slf4j
@Component
public class RemoteCodeSandbox implements CodeSandbox {
    @Resource
    private RemoteCodeSandboxClient remoteCodeSandboxClient;

    @Value("${code-sandbox.remote.x-service-key}")
    private String xServiceKey;

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        return remoteCodeSandboxClient.executeCode(xServiceKey, executeCodeRequest);
    }
}
