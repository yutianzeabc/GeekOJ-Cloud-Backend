package cc.geektip.geekoj.judgeservice.judge.strategy.impl;

import cc.geektip.geekoj.api.codesandbox.vo.JudgeInfo;
import cc.geektip.geekoj.api.model.dto.question.JudgeCase;
import cc.geektip.geekoj.api.model.dto.question.JudgeConfig;
import cc.geektip.geekoj.api.model.entity.problem.Question;
import cc.geektip.geekoj.api.model.enums.JudgeInfoEnum;
import cc.geektip.geekoj.judgeservice.judge.strategy.JudgeContext;
import cc.geektip.geekoj.judgeservice.judge.strategy.JudgeStrategy;
import cn.hutool.json.JSONUtil;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @description: 默认判题策略，用于定义默认的判题逻辑，如果没有特殊的判题逻辑，可以使用该策略进行判题
 * @author: Fish
 * @date: 2024/2/29
 */
@Component
public class DefaultJudgeStrategy implements JudgeStrategy {

    /**
     * 执行判题
     * @param judgeContext 判题上下文
     * @return 判题结果
     */
    @Override
    public JudgeInfo doJudge(JudgeContext judgeContext) {
        JudgeInfo judgeInfo = judgeContext.getJudgeInfo();
        Long memory = judgeInfo.getMemory();
        Long time = judgeInfo.getTime();
        List<String> inputList = judgeContext.getInputList();
        List<String> outputList = judgeContext.getOutputList();
        Question question = judgeContext.getQuestion();
        List<JudgeCase> judgeCaseList = judgeContext.getJudgeCaseList();
        JudgeInfoEnum judgeInfoEnum = JudgeInfoEnum.ACCEPTED;
        JudgeInfo judgeInfoResponse = new JudgeInfo();
        judgeInfoResponse.setMemory(memory);
        judgeInfoResponse.setTime(time);
        // 先判断沙箱执行的结果输出数量是否和预期输出数量相等
        if (outputList.size() != inputList.size()) {
            judgeInfoEnum = JudgeInfoEnum.WRONG_ANSWER;
            judgeInfoResponse.setMessage(judgeInfoEnum.getValue());
            return judgeInfoResponse;
        }
        // 依次判断每一项输出和预期输出是否相等
        for (int i = 0; i < judgeCaseList.size(); i++) {
            JudgeCase judgeCase = judgeCaseList.get(i);
            if (!judgeCase.getOutput().equals(outputList.get(i))) {
                judgeInfoEnum = JudgeInfoEnum.WRONG_ANSWER;
                judgeInfoResponse.setMessage(judgeInfoEnum.getValue());
                return judgeInfoResponse;
            }
        }
        // 判断题目限制
        String judgeConfigStr = question.getJudgeConfig();
        JudgeConfig judgeConfig = JSONUtil.toBean(judgeConfigStr, JudgeConfig.class);
        Long needMemoryLimit = judgeConfig.getMemoryLimit();
        Long needTimeLimit = judgeConfig.getTimeLimit();
        if (memory > needMemoryLimit) {
            judgeInfoEnum = JudgeInfoEnum.MEMORY_LIMIT_EXCEEDED;
            judgeInfoResponse.setMessage(judgeInfoEnum.getValue());
            return judgeInfoResponse;
        }
        if (time > needTimeLimit) {
            judgeInfoEnum = JudgeInfoEnum.TIME_LIMIT_EXCEEDED;
            judgeInfoResponse.setMessage(judgeInfoEnum.getValue());
            return judgeInfoResponse;
        }
        judgeInfoResponse.setMessage(judgeInfoEnum.getValue());
        return judgeInfoResponse;
    }

}
