package cc.geektip.geekoj.api.model.dto.question;

import lombok.Data;

import jakarta.validation.constraints.Min;

import java.io.Serial;
import java.io.Serializable;

/**
 * 创建请求
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
    private Long questionId;

    @Serial
    private static final long serialVersionUID = 1L;
}