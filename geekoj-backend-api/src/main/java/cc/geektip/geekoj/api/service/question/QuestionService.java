package cc.geektip.geekoj.api.service.question;

import cc.geektip.geekoj.api.model.dto.question.QuestionQueryRequest;
import cc.geektip.geekoj.api.model.entity.question.Question;
import cc.geektip.geekoj.api.model.vo.question.SafeQuestionVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author Fish
* @description 针对表【question(题目)】的数据库操作Service*/
public interface QuestionService extends IService<Question> {
    /**
     * 获取查询条件
     *
     * @param questionQueryRequest
     * @return
     */
    QueryWrapper<Question> getQueryWrapper(QuestionQueryRequest questionQueryRequest);

    /**
     * 对象转包装类
     * @param Question
     * @return
     */
    SafeQuestionVo objToVo(Question Question, Long uid);

    /**
     * 获取题目标签
     * @return
     */
    List<String> getQuestionTags();

    /**
     * 增加题目提交数
     *
     */
    void incrQuestionSubmitCount(Long questionId);
    /**
     * 增加题目通过数
     *
     */
    void incrQuestionAcceptedCount(Long questionId);
}
