package cc.geektip.geekoj.api.model.vo.question;

import lombok.Data;

import java.io.Serializable;

/**
 * 提交统计
 *
 */
@Data
public class QuestionSubmitSummaryVo implements Serializable {
    //题库总数
    private Long total;
    //简单
    private Long easyPass;
    private Long easyTotal;
    //中等
    private Long mediumPass;
    private Long mediumTotal;

    //困难
    private Long hardPass;
    private Long hardTotal;

    //提交总数
    private Long submitCount;
    //通过总数
    private Long passCount;
}
