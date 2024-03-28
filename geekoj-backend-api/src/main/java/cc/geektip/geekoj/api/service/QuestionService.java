package cc.geektip.geekoj.api.service;

import cc.geektip.geekoj.api.model.dto.question.QuestionQueryRequest;
import cc.geektip.geekoj.api.model.entity.problem.Question;
import cc.geektip.geekoj.api.model.vo.QuestionVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 问题服务
 *
 */
public interface QuestionService extends IService<Question> {

    /**
     * 校验
     *
     * @param question
     * @param add
     */
    void validQuestion(Question question, boolean add);

    /**
     * 获取查询条件
     *
     * @param questionQueryRequest
     * @return
     */
    QueryWrapper<Question> getQueryWrapper(QuestionQueryRequest questionQueryRequest);

    /**
     * 获取题目封装
     *
     * @param question
     * @return
     */
    QuestionVO getQuestionVO(Question question);

    /**
     * 分页获取题目封装
     *
     * @param questionPage
     * @return
     */
    Page<QuestionVO> getQuestionVOPage(Page<Question> questionPage);
}
