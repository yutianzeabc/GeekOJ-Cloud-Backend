package cc.geektip.geekoj.api.model.dto.ai;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: AI分析响应
 * @author: Bill Yu
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AiAnalyseResponse {

    /**
     * AI分析结果
     */
    private String aiAnalyseResult;

}
