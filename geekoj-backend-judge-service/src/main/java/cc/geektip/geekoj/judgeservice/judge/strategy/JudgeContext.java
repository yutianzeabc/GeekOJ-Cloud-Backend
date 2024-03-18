package cc.geektip.geekoj.judgeservice.judge.strategy;

import cc.geektip.geekoj.api.model.entity.Question;
import cc.geektip.geekoj.api.model.codesandbox.JudgeInfo;
import cc.geektip.geekoj.api.model.dto.question.JudgeCase;
import cc.geektip.geekoj.api.model.entity.QuestionSubmit;
import lombok.Data;

import java.util.List;

/**
 * @description: 判题上下文，用于判题策略之间的通信和数据传递
 * @author: Fish
 * @date: 2024/2/29
 */
@Data
public class JudgeContext {

    private JudgeInfo judgeInfo;

    private List<String> inputList;

    private List<String> outputList;

    private List<JudgeCase> judgeCaseList;

    private Question question;

    private QuestionSubmit questionSubmit;

}
