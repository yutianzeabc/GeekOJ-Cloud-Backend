package cc.geektip.geekoj.judgeservice.judge.strategy;

import cc.geektip.geekoj.api.model.dto.codesandbox.ExecuteCodeResponse;
import cc.geektip.geekoj.api.model.dto.judge.JudgeCase;
import cc.geektip.geekoj.api.model.entity.question.Question;
import cc.geektip.geekoj.api.model.entity.question.QuestionSubmit;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @description: 判题上下文，用于判题策略之间的通信和数据传递
 * @author: Fish
 *
 */
@Data
@Builder
public class JudgeContext {
    private List<String> inputList;
    private List<JudgeCase> judgeCaseList;
    private ExecuteCodeResponse executeCodeResponse;
    private Question question;
    private QuestionSubmit questionSubmit;
}
