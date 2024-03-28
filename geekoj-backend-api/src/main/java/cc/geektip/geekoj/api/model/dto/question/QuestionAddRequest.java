package cc.geektip.geekoj.api.model.dto.question;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 创建请求
 *
 */
@Data
public class QuestionAddRequest implements Serializable {

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
     * 难度
     */
    @NotBlank
    private String difficulty;

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
