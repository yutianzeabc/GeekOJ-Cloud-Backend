package cc.geektip.geekoj.api.service.judge;

import cc.geektip.geekoj.api.model.dto.question.QuestionRunRequest;
import cc.geektip.geekoj.api.model.entity.question.QuestionSubmit;
import cc.geektip.geekoj.api.model.vo.question.QuestionRunResult;

/**
 * @author Bill Yu
 * @description 判题服务
 */
public interface JudgeService {

    /**
     * 判题
     * @param questionSubmitId
     * @return
     */
    QuestionSubmit doJudge(Long questionSubmitId);

    /**
     * 执行
     * @param questionRunRequest
     * @return
     */
    QuestionRunResult doRun(QuestionRunRequest questionRunRequest);
}
