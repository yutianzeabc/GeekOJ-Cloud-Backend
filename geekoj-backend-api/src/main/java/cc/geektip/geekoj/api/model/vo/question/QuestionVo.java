package cc.geektip.geekoj.api.model.vo.question;

import cc.geektip.geekoj.api.model.dto.judge.JudgeCase;
import cc.geektip.geekoj.api.model.dto.judge.JudgeConfig;
import cc.geektip.geekoj.api.model.entity.question.Question;
import cn.hutool.json.JSONUtil;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 题目封装类
 * @TableName question
 */
@Data
public class QuestionVo implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 难度
     */
    private String difficulty;

    /**
     * 标签列表
     */
    private List<String> tags;

    /**
     * 题解
     */
    private String answer;

    /**
     * 题目提交数
     */
    private Integer submitNum;

    /**
     * 题目通过数
     */
    private Integer acceptedNum;

    /**
     * 测试用例（json 对象）
     */
    private List<JudgeCase> judgeCase;

    /**
     * 判题配置（json 对象）
     */
    private JudgeConfig judgeConfig;

    /**
     * 点赞数
     */
    private Integer thumbNum;

    /**
     * 收藏数
     */
    private Integer favourNum;

    /**
     * 创建者
     */
    private Long userId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 对象转包装类
     * @param Question
     * @return
     */
    public static QuestionVo objToVo(Question Question) {
        if (Question == null) {
            return null;
        }
        QuestionVo questionVo = new QuestionVo();
        BeanUtils.copyProperties(Question, questionVo);
        questionVo.setTags(JSONUtil.toList(Question.getTags(), String.class));
        questionVo.setJudgeConfig(JSONUtil.toBean(Question.getJudgeConfig(), JudgeConfig.class));
        questionVo.setJudgeCase(JSONUtil.toList(Question.getJudgeCase(), JudgeCase.class));
        return questionVo;
    }

    @Serial
    private static final long serialVersionUID = 1L;
}