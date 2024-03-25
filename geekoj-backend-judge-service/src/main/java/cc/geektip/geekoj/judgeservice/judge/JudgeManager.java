package cc.geektip.geekoj.judgeservice.judge;

import cc.geektip.geekoj.api.model.codesandbox.JudgeInfo;
import cc.geektip.geekoj.api.model.entity.QuestionSubmit;
import cc.geektip.geekoj.judgeservice.judge.strategy.JudgeContext;
import cc.geektip.geekoj.judgeservice.judge.strategy.JudgeStrategy;
import cc.geektip.geekoj.judgeservice.judge.strategy.JudgeStrategyRegistry;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @description: 判题管理器，用于管理判题服务和判题策略，单例
 * @author: Fish
 * @date: 2024/2/29
 */
@Service
public class JudgeManager {

    @Resource
    JudgeStrategyRegistry judgeStrategyRegistry;

    /**
     * 执行判题
     *
     * @param judgeContext 判题上下文
     * @return 判题结果
     */
    public JudgeInfo doJudge(JudgeContext judgeContext) {
        QuestionSubmit questionSubmit = judgeContext.getQuestionSubmit();
        String language = questionSubmit.getLanguage();
        JudgeStrategy judgeStrategy = judgeStrategyRegistry.getInstance(language);
        return judgeStrategy.doJudge(judgeContext);
    }

}
