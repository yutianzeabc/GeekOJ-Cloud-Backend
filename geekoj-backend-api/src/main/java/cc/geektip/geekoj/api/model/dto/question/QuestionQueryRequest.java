package cc.geektip.geekoj.api.model.dto.question;


import cc.geektip.geekoj.common.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 查询请求
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QuestionQueryRequest extends PageRequest implements Serializable {
    /**
     * 标签列表
     */
    private List<String> tags;
    private String status;
    private String difficulty;
    private String keyword;

    @Serial
    private static final long serialVersionUID = 1L;
}