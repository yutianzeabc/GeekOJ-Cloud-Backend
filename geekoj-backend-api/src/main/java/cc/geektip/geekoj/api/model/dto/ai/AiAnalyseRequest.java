package cc.geektip.geekoj.api.model.dto.ai;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: AI分析请求
 * @author: Bill Yu
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AiAnalyseRequest {

    @Min(1)
    private Integer questionId;
    @Min(1)
    private Integer questionSubmitId;

}
