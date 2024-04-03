package cc.geektip.geekoj.judgeservice.judge.codesandbox.impl;

import cc.geektip.geekoj.api.model.dto.codesandbox.ExecuteCodeRequest;
import cc.geektip.geekoj.api.model.dto.codesandbox.ExecuteCodeResponse;
import cc.geektip.geekoj.api.model.enums.question.QuestionSubmitStatusEnum;
import cc.geektip.geekoj.judgeservice.judge.codesandbox.CodeSandbox;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * @description: 示例代码沙箱
 * @author: Fish
 *
 */
@Slf4j
@Component
public class ExampleCodeSandbox implements CodeSandbox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest request) {
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        executeCodeResponse.setResults(new ArrayList<>());
        executeCodeResponse.setMsg("测试执行成功");
        executeCodeResponse.setCode(QuestionSubmitStatusEnum.SUCCEED.getValue());
        return executeCodeResponse;
    }
}
