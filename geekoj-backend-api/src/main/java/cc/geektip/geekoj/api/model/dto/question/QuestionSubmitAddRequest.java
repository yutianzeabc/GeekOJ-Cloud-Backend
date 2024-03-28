package cc.geektip.geekoj.api.model.dto.question;

import jakarta.validation.constraints.Min;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 创建请求
 *
 */
@Data
public class QuestionSubmitAddRequest implements Serializable {

    /**
     * 编程语言
     */
    private String language;

    /**
     * 用户代码
     */
    private String code;

    /**
     * 题目 id
     */
    @Min(1)
    private Long problemId;

    @Serial
    private static final long serialVersionUID = 1L;
}
