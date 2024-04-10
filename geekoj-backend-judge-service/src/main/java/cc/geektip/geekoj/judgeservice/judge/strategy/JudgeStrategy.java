package cc.geektip.geekoj.judgeservice.judge.strategy;

import cc.geektip.geekoj.api.model.vo.judge.JudgeInfo;

/**
 * @description: 判题策略接口，用于定义判题策略的执行方法，每个判题策略都需要实现该接口，并实现doJudge方法，用于执行判题逻辑
 * @author: Bill Yu
 *
 */
public interface JudgeStrategy {

    /**
     * 执行判题
     * @param judgeContext
     * @return
     */
    JudgeInfo doJudge(JudgeContext judgeContext);
}