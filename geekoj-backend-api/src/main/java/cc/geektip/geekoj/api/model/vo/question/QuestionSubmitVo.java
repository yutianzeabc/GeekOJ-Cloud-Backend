package cc.geektip.geekoj.api.model.vo.question;

import cc.geektip.geekoj.api.model.vo.judge.JudgeInfo;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 题目提交封装类
 *
 */
@Data
public class QuestionSubmitVo implements Serializable {
    private Long id;
    private String language;
    private String code;

    /**
     * 判题信息
     */
    private JudgeInfo judgeInfo;

    /**
     * 判题状态（0 - 待判题、1 - 判题中、2 - 成功、3 - 失败）
     */
    private Integer status;

    private Long questionId;
    private String questionTitle;
    private Long userId;
    private Date createTime;
    private Date updateTime;

    @Serial
    private static final long serialVersionUID = 1L;
}