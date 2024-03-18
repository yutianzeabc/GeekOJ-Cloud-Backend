package cc.geektip.geekoj.api.service;

import cc.geektip.geekoj.api.model.entity.QuestionSubmit;

/**
 * 判题服务
 *
 */
public interface JudgeService {
    /**
     * 判题
     * @param questionSubmitId 提交ID
     * @return 判题结果
     */
    QuestionSubmit doJudge(long questionSubmitId);
}
