package cc.geektip.geekoj.questionservice.service.impl;

import cc.geektip.geekoj.api.model.dto.question.QuestionRunRequest;
import cc.geektip.geekoj.api.model.vo.question.QuestionRunResult;
import cc.geektip.geekoj.api.service.judge.JudgeService;
import cc.geektip.geekoj.api.service.question.QuestionRunService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;

@DubboService
public class QuestionRunServiceImpl implements QuestionRunService {
    @DubboReference
    private JudgeService judgeService;

    @Override
    public QuestionRunResult runQuestionNow(QuestionRunRequest questionRunRequest) {
        return judgeService.doRun(questionRunRequest);
    }
}
