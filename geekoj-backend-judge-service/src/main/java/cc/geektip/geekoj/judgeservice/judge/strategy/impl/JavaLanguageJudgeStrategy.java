package cc.geektip.geekoj.judgeservice.judge.strategy.impl;

import cc.geektip.geekoj.api.model.dto.codesandbox.ExecuteCodeResponse;
import cc.geektip.geekoj.api.model.dto.codesandbox.ExecuteResult;
import cc.geektip.geekoj.api.model.dto.judge.JudgeCase;
import cc.geektip.geekoj.api.model.dto.judge.JudgeConfig;
import cc.geektip.geekoj.api.model.entity.question.Question;
import cc.geektip.geekoj.api.model.enums.codesandbox.ExecuteCodeStatusEnum;
import cc.geektip.geekoj.api.model.enums.judge.JudgeInfoEnum;
import cc.geektip.geekoj.api.model.vo.judge.JudgeInfo;
import cc.geektip.geekoj.judgeservice.judge.strategy.JudgeContext;
import cc.geektip.geekoj.judgeservice.judge.strategy.JudgeStrategy;
import cn.hutool.json.JSONUtil;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @description: Java 语言判题策略，用于定义 Java 语言的判题逻辑
 * @author: Bill Yu
 *
 */
@Component
public class JavaLanguageJudgeStrategy implements JudgeStrategy {

    private static final long JAVA_PROGRAM_TIME_LIMIT_MULTIPLIER = 2L;

    /**
     * 执行判题
     * @param judgeContext
     * @return
     */
    @Override
    public JudgeInfo doJudge(JudgeContext judgeContext) {
        ExecuteCodeResponse response = judgeContext.getExecuteCodeResponse();
        List<JudgeCase> judgeCaseList = judgeContext.getJudgeCaseList();

        JudgeInfo judgeInfo = new JudgeInfo();
        int total = judgeCaseList.size();
        judgeInfo.setTotal(total);

        if (response.getCode().equals(ExecuteCodeStatusEnum.SUCCESS.getValue())) {
            List<ExecuteResult> results = response.getResults();
            List<String> inputList = judgeContext.getInputList();
            List<String> outputList = results.stream().map(ExecuteResult::getOutput).toList();
            List<String> expectedOutputList = judgeCaseList.stream().map(JudgeCase::getOutput).toList();
            Question question = judgeContext.getQuestion();
            JudgeConfig judgeConfig = JSONUtil.toBean(question.getJudgeConfig(), JudgeConfig.class);

            int pass = 0;
            long maxTime = 0L;
            long maxMemory = 0L;

            for (int i = 0; i < total; i++) {
                // 判断执行时间和内存
                long time = results.get(i).getTime();
                long memory = results.get(i).getMemory();
                maxTime = Math.max(maxTime, time);
                maxMemory = Math.max(maxMemory, memory);
                // 判断输出是否正确
                if (expectedOutputList.get(i).equals(outputList.get(i))) {
                    // 超时, 乘以 2 是因为 Java 程序的执行时间通常会比较长
                    if (maxTime > judgeConfig.getTimeLimit() * JAVA_PROGRAM_TIME_LIMIT_MULTIPLIER) {
                        judgeInfo.setTime(maxTime);
                        judgeInfo.setMemory(maxMemory);
                        judgeInfo.setPass(pass);
                        judgeInfo.setStatus(JudgeInfoEnum.TIME_LIMIT_EXCEEDED.getValue());
                        judgeInfo.setMessage(JudgeInfoEnum.TIME_LIMIT_EXCEEDED.getText());
                        break;
                        // 超内存
                    } else if (maxMemory > judgeConfig.getMemoryLimit()) {
                        judgeInfo.setTime(maxTime);
                        judgeInfo.setMemory(maxMemory);
                        judgeInfo.setPass(pass);
                        judgeInfo.setStatus(JudgeInfoEnum.MEMORY_LIMIT_EXCEEDED.getValue());
                        judgeInfo.setMessage(JudgeInfoEnum.MEMORY_LIMIT_EXCEEDED.getText());
                        break;
                    } else {
                        pass++;
                    }
                    // 错误答案
                } else {
                    judgeInfo.setPass(pass);
                    judgeInfo.setTime(maxTime);
                    judgeInfo.setMemory(maxMemory);
                    judgeInfo.setStatus(JudgeInfoEnum.WRONG_ANSWER.getValue());
                    judgeInfo.setMessage(JudgeInfoEnum.WRONG_ANSWER.getText());
                    judgeInfo.setInput(inputList.get(i));
                    judgeInfo.setOutput(outputList.get(i));
                    judgeInfo.setExpectedOutput(expectedOutputList.get(i));
                    break;
                }
            }
            if (pass == total) {
                judgeInfo.setPass(total);
                judgeInfo.setTime(maxTime);
                judgeInfo.setMemory(maxMemory);
                judgeInfo.setStatus(JudgeInfoEnum.ACCEPTED.getValue());
                judgeInfo.setMessage(JudgeInfoEnum.ACCEPTED.getText());
            }
        } else if (response.getCode().equals(ExecuteCodeStatusEnum.COMPILE_FAILED.getValue())) {
            judgeInfo.setPass(0);
            judgeInfo.setStatus(JudgeInfoEnum.COMPILE_ERROR.getValue());
            judgeInfo.setMessage(JudgeInfoEnum.COMPILE_ERROR.getText() + ":\n" + response.getMsg());
        } else if (response.getCode().equals(ExecuteCodeStatusEnum.RUN_FAILED.getValue())) {
            judgeInfo.setPass(0);
            judgeInfo.setStatus(JudgeInfoEnum.RUNTIME_ERROR.getValue());
            judgeInfo.setMessage(JudgeInfoEnum.RUNTIME_ERROR.getText()+ ":\n" + response.getMsg());
        }

        return judgeInfo;
    }

}
