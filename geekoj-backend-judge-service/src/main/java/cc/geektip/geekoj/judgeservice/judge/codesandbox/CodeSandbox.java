package cc.geektip.geekoj.judgeservice.judge.codesandbox;

import cc.geektip.geekoj.api.model.dto.codesandbox.ExecuteCodeRequest;
import cc.geektip.geekoj.api.model.dto.codesandbox.ExecuteCodeResponse;

/**
 * @description: 代码沙箱接口
 * @author: Bill Yu
 *
 */
public interface CodeSandbox {
    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);
}
