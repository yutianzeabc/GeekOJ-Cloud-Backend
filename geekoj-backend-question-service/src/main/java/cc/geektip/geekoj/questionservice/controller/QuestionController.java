package cc.geektip.geekoj.questionservice.controller;

import cc.geektip.geekoj.api.model.dto.question.QuestionAddRequest;
import cc.geektip.geekoj.api.model.dto.question.QuestionEditRequest;
import cc.geektip.geekoj.api.model.dto.question.QuestionQueryRequest;
import cc.geektip.geekoj.api.model.dto.question.QuestionUpdateRequest;
import cc.geektip.geekoj.api.model.entity.question.Question;
import cc.geektip.geekoj.api.model.vo.question.QuestionVo;
import cc.geektip.geekoj.api.model.vo.question.SafeQuestionVo;
import cc.geektip.geekoj.api.service.question.QuestionService;
import cc.geektip.geekoj.common.common.R;
import cc.geektip.geekoj.common.constant.UserConstant;
import cn.dev33.satoken.annotation.SaCheckRole;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 题目接口
 */
@RestController
@RequestMapping("/question")
@Validated
@Slf4j
public class QuestionController {
    @Resource
    private QuestionService questionService;

    /**
     * 创建
     *
     * @param questionAddRequest
     * @return
     */
    @PostMapping("/add")
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    public R<Long> addQuestion(@RequestBody @NotNull @Valid QuestionAddRequest questionAddRequest) {
        Question question = questionService.addQuestion(questionAddRequest);
        return R.ok(question.getId());
    }

    /**
     * 分页获取题目列表（仅管理员）
     *
     * @param questionQueryRequest
     * @return
     */
    @PostMapping("/page/vo")
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    public R<Page<QuestionVo>> listQuestionVoByPage(@RequestBody @NotNull QuestionQueryRequest questionQueryRequest) {
        Page<QuestionVo> page = questionService.listQuestionVoByPage(questionQueryRequest);
        return R.ok(page);
    }

    /**
     * 更新（仅管理员）
     *
     * @param questionUpdateRequest
     * @return
     */
    @PutMapping
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    public R<Boolean> updateQuestion(@RequestBody @NotNull @Valid QuestionUpdateRequest questionUpdateRequest) {
        boolean result = questionService.updateQuestion(questionUpdateRequest);
        return R.ok(result);
    }


    /**
     * 根据 id 获取完整信息（仅管理员，包含测试用例）
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}/vo")
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    public R<QuestionVo> getQuestionVoById(@PathVariable("id") @Min(1) Long id) {
        QuestionVo questionVo = questionService.getQuestionVoById(id);
        return R.ok(questionVo);
    }

    /**
     * 删除（仅管理员）
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    public R<Boolean> deleteQuestion(@PathVariable("id") @Min(1) Long id) {
        boolean result = questionService.deleteQuestion(id);
        return R.ok(result);
    }

    /**
     * 编辑（仅管理员）
     *
     * @param questionEditRequest
     * @return
     */
    @PostMapping("/edit")
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    public R<Boolean> editQuestion(@RequestBody @NotNull @Valid QuestionEditRequest questionEditRequest) {
        boolean result = questionService.editQuestion(questionEditRequest);
        return R.ok(result);
    }

    /**
     * 根据 id 获取（脱敏）
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}/vo/safe")
    public R<SafeQuestionVo> getSafeQuestionVoById(@PathVariable("id") @Min(1) Long id) {
        SafeQuestionVo vo = questionService.getSafeQuestionVoById(id);
        return R.ok(vo);
    }

    /**
     * 分页获取列表（封装类）
     *
     * @param questionQueryRequest
     * @return
     */
    @PostMapping("/page/vo/safe")
    public R<Page<SafeQuestionVo>> listSafeQuestionVoByPage(@RequestBody QuestionQueryRequest questionQueryRequest) {
        Page<SafeQuestionVo> page = questionService.listSafeQuestionVoByPage(questionQueryRequest);
        return R.ok(page);
    }

    /**
     * 获取所有标签
     *
     * @return
     */
    @GetMapping("/tags")
    public R<List<String>> getQuestionTags() {
        List<String> tags = questionService.getQuestionTags();
        return R.ok(tags);
    }
}
