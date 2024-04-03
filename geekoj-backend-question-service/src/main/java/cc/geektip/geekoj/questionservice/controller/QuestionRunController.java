package cc.geektip.geekoj.questionservice.controller;

import cc.geektip.geekoj.api.model.dto.question.QuestionRunRequest;
import cc.geektip.geekoj.api.model.vo.question.QuestionRunResult;
import cc.geektip.geekoj.api.service.question.QuestionRunService;
import cc.geektip.geekoj.common.common.R;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;

/**
 * 题目接口
 */
@RestController
@RequestMapping("/question_run")
@Validated
@Slf4j
public class QuestionRunController {
    @Resource
    private QuestionRunService questionRunService;

    @PostMapping
    public R<QuestionRunResult> doQuestionRun(@RequestBody @NotNull @Valid QuestionRunRequest questionRunRequest) {
        QuestionRunResult questionRunResult = questionRunService.runQuestionNow(questionRunRequest);
        return R.ok(questionRunResult);
    }

}
