package cc.geektip.geekoj.api.model.dto.question;

import cc.geektip.geekoj.common.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * 查询请求
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QuestionSubmitQueryRequest extends PageRequest implements Serializable {
    /**
     * 题目 id
     */
    private Long QuestionId;

    @Serial
    private static final long serialVersionUID = 1L;
}