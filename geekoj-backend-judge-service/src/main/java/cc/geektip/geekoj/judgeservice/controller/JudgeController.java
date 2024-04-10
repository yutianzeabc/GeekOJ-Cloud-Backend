package cc.geektip.geekoj.judgeservice.controller;

import cc.geektip.geekoj.api.model.dto.question.QuestionRunRequest;
import cc.geektip.geekoj.api.model.vo.question.QuestionRunResult;
import cc.geektip.geekoj.api.service.judge.JudgeService;
import cc.geektip.geekoj.common.common.R;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class JudgeController {
    @Resource
    private JudgeService judgeService;

    @PostMapping("/run")
    public R<QuestionRunResult> doRun(@RequestBody @NotNull @Valid QuestionRunRequest questionRunRequest) {
        QuestionRunResult questionRunResult = judgeService.doRun(questionRunRequest);
        return R.ok(questionRunResult);
    }
}
