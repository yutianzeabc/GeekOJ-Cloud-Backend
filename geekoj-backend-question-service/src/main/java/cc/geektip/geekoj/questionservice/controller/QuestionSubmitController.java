package cc.geektip.geekoj.questionservice.controller;


import cc.geektip.geekoj.api.model.dto.question.QuestionSubmitAddRequest;
import cc.geektip.geekoj.api.model.dto.question.QuestionSubmitQueryRequest;
import cc.geektip.geekoj.api.model.entity.question.QuestionSubmit;
import cc.geektip.geekoj.api.model.vo.question.QuestionSubmitSummaryVo;
import cc.geektip.geekoj.api.model.vo.question.QuestionSubmitVo;
import cc.geektip.geekoj.api.service.question.QuestionSubmitService;
import cc.geektip.geekoj.common.common.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 题目提交接口
 */
@RestController
@RequestMapping("/question_submit")
@Slf4j
@Validated
public class QuestionSubmitController {
    @Resource
    private QuestionSubmitService questionSubmitService;

    @GetMapping("/summary")
    public R<QuestionSubmitSummaryVo> getSubmitSummary(){
        QuestionSubmitSummaryVo vo = questionSubmitService.getSubmitSummary();
        return R.ok(vo);
    }

    /**
     * 提交题目
     * @param questionSubmitAddRequest
     * @return 提交记录的 id
     */
    @PostMapping
    public R<QuestionSubmit> doQuestionSubmit(@RequestBody @NotNull @Valid QuestionSubmitAddRequest questionSubmitAddRequest) {
        QuestionSubmit submitResult = questionSubmitService.doQuestionSubmit(questionSubmitAddRequest);
        return R.ok(submitResult);
    }

    /**
     * 分页获取题目提交历史（全部）
     * @param questionSubmitQueryRequest
     * @return
     */
    @PostMapping("/page/vo")
    public R<Page<QuestionSubmitVo>> listAllQuestionSubmitVoByPage(@RequestBody @NotNull QuestionSubmitQueryRequest questionSubmitQueryRequest) {
        Page<QuestionSubmitVo> page = questionSubmitService.listAllQuestionSubmitVoByPage(questionSubmitQueryRequest);
        // 返回脱敏信息
        return R.ok(page);
    }

    /**
     * 分页获取题目提交历史（个人）
     * @param questionSubmitQueryRequest
     * @return
     */
    @PostMapping("/page/vo/my")
    public R<Page<QuestionSubmitVo>> listMyQuestionSubmitVoByPage(@RequestBody @NotNull QuestionSubmitQueryRequest questionSubmitQueryRequest) {
        Page<QuestionSubmitVo> page = questionSubmitService.listMyQuestionSubmitVoByPage(questionSubmitQueryRequest);
        // 返回脱敏信息
        return R.ok(page);
    }

    /**
     * 获取某次历史提交的详细信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<QuestionSubmitVo> getQuestionSubmitVoById(@PathVariable("id") Long id) {
        QuestionSubmitVo questionSubmitVo = questionSubmitService.getQuestionSubmitVoById(id);
        // 返回脱敏信息
        return R.ok(questionSubmitVo);
    }
}
