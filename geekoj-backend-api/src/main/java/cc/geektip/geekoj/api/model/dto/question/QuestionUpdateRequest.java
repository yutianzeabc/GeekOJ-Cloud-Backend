package cc.geektip.geekoj.api.model.dto.question;

import cc.geektip.geekoj.api.model.dto.judge.JudgeCase;
import cc.geektip.geekoj.api.model.dto.judge.JudgeConfig;
import lombok.Data;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 更新请求
 */
@Data
public class QuestionUpdateRequest implements Serializable {


    /**
     * id
     */
    @Min(1)
    private Long id;

    /**
     * 标题
     */
    @NotBlank(message = "标题不能为空")
    @Length(max = 80, message = "标题过长")
    private String title;

    /**
     * 内容
     */
    @NotBlank(message = "内容不能为空")
    @Length(max = 8192, message = "内容过长")
    private String content;

    /**
     * 难度
     */
    @NotBlank(message = "难度不能为空")
    private String difficulty;

    /**
     * 标签列表
     */
    @NotNull(message = "标签不能为空")
    private List<String> tags;

    /**
     * 题目答案
     */
    @Length(max = 8192, message = "答案过长")
    private String answer;

    /**
     * 判题用例
     */
    @NotNull(message = "判题用例不能为空")
    private List<JudgeCase> judgeCase;

    /**
     * 判题配置
     */
    @NotNull(message = "判题配置不能为空")
    private JudgeConfig judgeConfig;

    @Serial
    private static final long serialVersionUID = 1L;
}