package cc.geektip.geekoj.judgeservice.judge.codesandbox.impl;

import cc.geektip.geekoj.api.model.codesandbox.ExecuteCodeRequest;
import cc.geektip.geekoj.api.model.codesandbox.ExecuteCodeResponse;
import cc.geektip.geekoj.api.model.codesandbox.JudgeInfo;
import cc.geektip.geekoj.api.model.enums.JudgeInfoMessageEnum;
import cc.geektip.geekoj.api.model.enums.QuestionSubmitStatusEnum;
import cc.geektip.geekoj.judgeservice.judge.codesandbox.CodeSandbox;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @description: 示例代码沙箱
 * @author: Fish
 * @date: 2024/2/28
 */
@Slf4j
@Component
public class ExampleCodeSandbox implements CodeSandbox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest request) {
        List<String> inputList = request.getInputList();
        String code = request.getCode();

        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        executeCodeResponse.setOutputList(inputList);
        executeCodeResponse.setMessage("测试执行成功");
        executeCodeResponse.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());
        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setMessage(JudgeInfoMessageEnum.ACCEPTED.getText());
        judgeInfo.setMemory(100L);
        judgeInfo.setTime(100L);
        executeCodeResponse.setJudgeInfo(judgeInfo);
        return executeCodeResponse;
    }
}
