package cc.geektip.geekoj.judgeservice.service.impl;

import cc.geektip.geekoj.api.model.dto.codesandbox.ExecuteCodeRequest;
import cc.geektip.geekoj.api.model.dto.codesandbox.ExecuteCodeResponse;
import cc.geektip.geekoj.api.model.dto.judge.JudgeCase;
import cc.geektip.geekoj.api.model.dto.question.QuestionRunRequest;
import cc.geektip.geekoj.api.model.entity.question.Question;
import cc.geektip.geekoj.api.model.entity.question.QuestionSubmit;
import cc.geektip.geekoj.api.model.enums.codesandbox.ExecuteCodeStatusEnum;
import cc.geektip.geekoj.api.model.enums.judge.JudgeInfoEnum;
import cc.geektip.geekoj.api.model.enums.question.QuestionSubmitStatusEnum;
import cc.geektip.geekoj.api.model.vo.judge.JudgeInfo;
import cc.geektip.geekoj.api.model.vo.question.QuestionRunResult;
import cc.geektip.geekoj.api.service.judge.JudgeService;
import cc.geektip.geekoj.api.service.question.QuestionService;
import cc.geektip.geekoj.api.service.question.QuestionSubmitService;
import cc.geektip.geekoj.common.common.AppHttpCodeEnum;
import cc.geektip.geekoj.common.exception.ThrowUtils;
import cc.geektip.geekoj.judgeservice.judge.JudgeManager;
import cc.geektip.geekoj.judgeservice.judge.codesandbox.CodeSandbox;
import cc.geektip.geekoj.judgeservice.judge.codesandbox.CodeSandboxProxy;
import cc.geektip.geekoj.judgeservice.judge.codesandbox.CodeSandboxRegistry;
import cc.geektip.geekoj.judgeservice.judge.strategy.JudgeContext;
import cn.hutool.json.JSONUtil;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.VirtualThreadTaskExecutor;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * @description: 判题服务
 * @author: Bill Yu
 */
@DubboService
public class JudgeServiceImpl implements JudgeService {
    @DubboReference
    private QuestionService questionService;

    @DubboReference
    private QuestionSubmitService questionSubmitService;

    @Resource
    private CodeSandboxRegistry codeSandboxRegistry;

    @Resource
    private JudgeManager judgeManager;


    @Value("${code-sandbox.type:remote}")
    private String type;

    public QuestionSubmit doJudge(Long questionSubmitId) {
        // 1. 传入题目的提交 id，获取到对应的题目、提交信息（包含代码、编程语言等）
        QuestionSubmit questionSubmit = questionSubmitService.getById(questionSubmitId);
        ThrowUtils.throwIf(questionSubmit == null, AppHttpCodeEnum.NOT_EXIST, "题目提交不存在");
        Long questionId = questionSubmit.getQuestionId();
        Question question = questionService.getById(questionId);
        ThrowUtils.throwIf(question == null, AppHttpCodeEnum.NOT_EXIST, "题目不存在");

        // 2. 更改判题（题目提交）的状态为 “判题中”，防止用户重复提交
        QuestionSubmit questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmit.getId());
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.RUNNING.getValue());
        boolean statusUpdateOk = questionSubmitService.updateById(questionSubmitUpdate);
        ThrowUtils.throwIf(!statusUpdateOk, AppHttpCodeEnum.INTERNAL_SERVER_ERROR, "题目状态更新失败");

        // 3. 调用沙箱，获取到执行结果
        CodeSandbox codeSandbox = new CodeSandboxProxy(codeSandboxRegistry.getInstance(type));
        String language = questionSubmit.getLanguage();
        String code = questionSubmit.getCode();
        // 获取输入用例
        List<JudgeCase> judgeCaseList = JSONUtil.toList(question.getJudgeCase(), JudgeCase.class);
        List<String> inputList = judgeCaseList.stream().map(JudgeCase::getInput).toList();
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                .code(code)
                .language(language)
                .inputList(inputList)
                .build();
        ExecuteCodeResponse executeCodeResponse = codeSandbox.executeCode(executeCodeRequest);
        // 4. 根据沙箱的执行结果，设置题目的判题状态和信息
        JudgeContext judgeContext = JudgeContext.builder()
                .inputList(inputList)
                .judgeCaseList(judgeCaseList)
                .executeCodeResponse(executeCodeResponse)
                .question(question)
                .questionSubmit(questionSubmit)
                .build();
        JudgeInfo judgeInfo = judgeManager.doJudge(judgeContext);

        // 5. 修改数据库中的判题结果
        boolean isJudgeAccepted = JudgeInfoEnum.ACCEPTED.getValue().equals(judgeInfo.getStatus());
        questionSubmitUpdate.setStatus(isJudgeAccepted ? QuestionSubmitStatusEnum.SUCCEED.getValue() : QuestionSubmitStatusEnum.FAILED.getValue());
        questionSubmitUpdate.setJudgeInfo(JSONUtil.toJsonStr(judgeInfo));
        boolean resultUpdateOk = questionSubmitService.updateById(questionSubmitUpdate);
        ThrowUtils.throwIf(!resultUpdateOk, AppHttpCodeEnum.INTERNAL_SERVER_ERROR, "题目状态更新失败");

        // 6. 修改题目的通过数
        if (isJudgeAccepted) questionService.incrQuestionAcceptedCount(questionId);

        return questionSubmitUpdate;
    }


    @Override
    public QuestionRunResult doRun(QuestionRunRequest questionRunRequest) {
        // 1. 获取题目的运行信息
        CodeSandbox codeSandbox = new CodeSandboxProxy(codeSandboxRegistry.getInstance(type));
        String language = questionRunRequest.getLanguage();
        String code = questionRunRequest.getCode();
        List<String> inputList = Collections.singletonList(questionRunRequest.getInput());

        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                .code(code)
                .language(language)
                .inputList(inputList)
                .build();
        ExecuteCodeResponse executeCodeResponse = codeSandbox.executeCode(executeCodeRequest);

        // 2. 根据沙箱的执行结果，设置题目的运行状态和信息
        QuestionRunResult questionRunResult = new QuestionRunResult();
        questionRunResult.setInput(questionRunRequest.getInput());
        questionRunResult.setCode(executeCodeResponse.getCode());
        if (ExecuteCodeStatusEnum.SUCCESS.getValue().equals(executeCodeResponse.getCode())) {
            questionRunResult.setOutput(executeCodeResponse.getResults().get(0).getOutput());
        } else {
            questionRunResult.setOutput(executeCodeResponse.getMsg());
        }
        return questionRunResult;
    }

}
