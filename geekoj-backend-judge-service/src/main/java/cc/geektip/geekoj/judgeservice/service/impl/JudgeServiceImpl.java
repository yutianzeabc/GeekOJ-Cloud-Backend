package cc.geektip.geekoj.judgeservice.service.impl;

import cc.geektip.geekoj.api.model.codesandbox.ExecuteCodeRequest;
import cc.geektip.geekoj.api.model.codesandbox.ExecuteCodeResponse;
import cc.geektip.geekoj.api.model.codesandbox.JudgeInfo;
import cc.geektip.geekoj.api.model.dto.question.JudgeCase;
import cc.geektip.geekoj.api.model.entity.Question;
import cc.geektip.geekoj.api.model.entity.QuestionSubmit;
import cc.geektip.geekoj.api.model.enums.QuestionSubmitStatusEnum;
import cc.geektip.geekoj.api.service.JudgeService;
import cc.geektip.geekoj.api.service.QuestionService;
import cc.geektip.geekoj.api.service.QuestionSubmitService;
import cc.geektip.geekoj.common.common.ErrorCode;
import cc.geektip.geekoj.common.exception.BusinessException;
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

import java.util.List;
import java.util.stream.Collectors;

/**
 * @description: 判题服务
 * @author: Fish
 * @date: 2024/2/28
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


    @Value("${codesandbox.type:example}")
    private String type;

    public QuestionSubmit doJudge(long questionSubmitId) {
        // 1）传入题目的提交 id，获取到对应的题目、提交信息（包含代码、编程语言等）
        QuestionSubmit questionSubmit = questionSubmitService.getById(questionSubmitId);
        if (questionSubmit == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "提交信息不存在");
        }
        Long questionId = questionSubmit.getQuestionId();
        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "题目不存在");
        }
        // 2）如果题目提交状态不为等待中，就不用重复执行了
        if (!questionSubmit.getStatus().equals(QuestionSubmitStatusEnum.WAITING.getValue())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "题目正在判题中");
        }
        // 3）更改判题（题目提交）的状态为 “判题中”，防止重复执行
        QuestionSubmit questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmitId);
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.RUNNING.getValue());
        boolean update = questionSubmitService.updateById(questionSubmitUpdate);
        if (!update) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "题目状态更新错误");
        }
        // 4）调用沙箱，获取到执行结果
        CodeSandbox codeSandbox = codeSandboxRegistry.getInstance(type);
        codeSandbox = new CodeSandboxProxy(codeSandbox);
        String language = questionSubmit.getLanguage();
        String code = questionSubmit.getCode();
        // 获取输入用例
        String judgeCaseStr = question.getJudgeCase();
        List<JudgeCase> judgeCaseList = JSONUtil.toList(judgeCaseStr, JudgeCase.class);
        List<String> inputList = judgeCaseList.stream().map(JudgeCase::getInput).collect(Collectors.toList());
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                .code(code)
                .language(language)
                .inputList(inputList)
                .build();
        ExecuteCodeResponse executeCodeResponse = codeSandbox.executeCode(executeCodeRequest);
        List<String> outputList = executeCodeResponse.getOutputList();
        // 5）根据沙箱的执行结果，设置题目的判题状态和信息
        JudgeContext judgeContext = new JudgeContext();
        judgeContext.setJudgeInfo(executeCodeResponse.getJudgeInfo());
        judgeContext.setInputList(inputList);
        judgeContext.setOutputList(outputList);
        judgeContext.setJudgeCaseList(judgeCaseList);
        judgeContext.setQuestion(question);
        judgeContext.setQuestionSubmit(questionSubmit);
        JudgeInfo judgeInfo = judgeManager.doJudge(judgeContext);
        // 6）修改数据库中的判题结果
        questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmitId);
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());
        questionSubmitUpdate.setJudgeInfo(JSONUtil.toJsonStr(judgeInfo));
        update = questionSubmitService.updateById(questionSubmitUpdate);
        if (!update) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "题目状态更新错误");
        }
        QuestionSubmit questionSubmitResult = questionSubmitService.getById(questionId);
        return questionSubmitResult;
    }

}
