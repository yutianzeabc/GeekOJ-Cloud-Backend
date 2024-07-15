package cc.geektip.geekoj.api.service.ai;

import cc.geektip.geekoj.api.model.dto.ai.AiAnalyseRequest;
import cc.geektip.geekoj.api.model.dto.ai.AiAnalyseResponse;

/**
 * @author Bill Yu
 * @description AI服务
 */
public interface AiService {

    /**
     * AI 分析答题结果
     *
     * @param aiAnalyseRequest AI分析请求
     * @return AI分析结果
     */
    AiAnalyseResponse doAnalyse(AiAnalyseRequest aiAnalyseRequest);

}
