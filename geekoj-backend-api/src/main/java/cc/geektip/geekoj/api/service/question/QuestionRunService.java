package cc.geektip.geekoj.api.service.question;

import cc.geektip.geekoj.api.model.dto.question.QuestionRunRequest;
import cc.geektip.geekoj.api.model.vo.question.QuestionRunResult;

public interface QuestionRunService {
    QuestionRunResult runQuestionNow(QuestionRunRequest QuestionRunRequest);
}
