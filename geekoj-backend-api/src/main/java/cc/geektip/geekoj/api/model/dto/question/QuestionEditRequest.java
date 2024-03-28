package cc.geektip.geekoj.api.model.dto.question;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 编辑请求
 *
 */
@Data
public class QuestionEditRequest implements Serializable {

    /**
     * id
     */
    @Min(1)
    private Long id;

    /**
     * 标题
     */
    @NotBlank
    private String title;

    /**
     * 内容
     */
    @NotBlank
    private String content;

    /**
     * 标签列表
     */
    @NotNull
    private List<String> tags;

    /**
     * 题目答案
     */
    private String answer;

    /**
     * 判题用例
     */
    @NotNull
    private List<JudgeCase> judgeCase;

    /**
     * 判题配置
     */
    @NotNull
    private JudgeConfig judgeConfig;

    @Serial
    private static final long serialVersionUID = 1L;
}
