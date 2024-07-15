package cc.geektip.geekoj.api.service.question;

import cc.geektip.geekoj.api.model.dto.question.QuestionAddRequest;
import cc.geektip.geekoj.api.model.dto.question.QuestionEditRequest;
import cc.geektip.geekoj.api.model.dto.question.QuestionQueryRequest;
import cc.geektip.geekoj.api.model.dto.question.QuestionUpdateRequest;
import cc.geektip.geekoj.api.model.entity.question.Question;
import cc.geektip.geekoj.api.model.vo.question.QuestionVo;
import cc.geektip.geekoj.api.model.vo.question.SafeQuestionVo;
import cc.geektip.geekoj.api.model.vo.user.UserInfoVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author Bill Yu
* @description 针对表【question(题目)】的数据库操作Service
*/
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
    SafeQuestionVo objToVo(Question Question, UserInfoVo currentUser);

    /**
     * 获取题目标题
     * @param questionId
     * @return
     */
    String getQuestionTitleById(Long questionId);

    /**
     * 获取题目标签
     * @return
     */
    List<String> getQuestionTags();

    /**
     * 获取题目列表
     * @param questionQueryRequest
     * @return
     */
    Page<QuestionVo> listQuestionVoByPage(QuestionQueryRequest questionQueryRequest);

    /**
     * 新增题目
     * @param questionAddRequest
     * @return
     */
    Question addQuestion(QuestionAddRequest questionAddRequest);

    /**
     * 更新题目
     * @param questionUpdateRequest
     * @return
     */
    boolean updateQuestion(QuestionUpdateRequest questionUpdateRequest);

    /**
     * 获取题目详情
     * @param id
     * @return
     */
    QuestionVo getQuestionVoById(Long id);

    /**
     * 删除题目
     * @param id
     * @return
     */
    boolean deleteQuestion(Long id);

    /**
     * 编辑题目
     * @param questionEditRequest
     * @return
     */
    boolean editQuestion(QuestionEditRequest questionEditRequest);

    /**
     * 获取题目详情（脱敏视图）
     * @param id
     * @return
     */
    SafeQuestionVo getSafeQuestionVoById(Long id);

    /**
     * 分页获取题目列表（脱敏视图）
     * @param questionQueryRequest
     * @return
     */
    Page<SafeQuestionVo> listSafeQuestionVoByPage(QuestionQueryRequest questionQueryRequest);

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
