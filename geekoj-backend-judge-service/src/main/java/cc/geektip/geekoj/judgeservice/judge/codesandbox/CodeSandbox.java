package cc.geektip.geekoj.judgeservice.judge.codesandbox;

import cc.geektip.geekoj.api.model.codesandbox.ExecuteCodeRequest;
import cc.geektip.geekoj.api.model.codesandbox.ExecuteCodeResponse;

/**
 * @description: 代码沙箱接口
 * @author: Fish
 * @date: 2024/2/28
 */
public interface CodeSandbox {
    ExecuteCodeResponse executeCode(ExecuteCodeRequest request);
}
