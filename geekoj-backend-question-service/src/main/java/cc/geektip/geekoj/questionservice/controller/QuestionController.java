package cc.geektip.geekoj.questionservice.controller;

import cc.geektip.geekoj.api.model.dto.judge.JudgeCase;
import cc.geektip.geekoj.api.model.dto.judge.JudgeConfig;
import cc.geektip.geekoj.api.model.dto.question.QuestionAddRequest;
import cc.geektip.geekoj.api.model.dto.question.QuestionEditRequest;
import cc.geektip.geekoj.api.model.dto.question.QuestionQueryRequest;
import cc.geektip.geekoj.api.model.dto.question.QuestionUpdateRequest;
import cc.geektip.geekoj.api.model.entity.question.Question;
import cc.geektip.geekoj.api.model.vo.question.QuestionVo;
import cc.geektip.geekoj.api.model.vo.question.SafeQuestionVo;
import cc.geektip.geekoj.api.model.vo.user.UserInfoVo;
import cc.geektip.geekoj.api.service.question.QuestionService;
import cc.geektip.geekoj.common.common.AppHttpCodeEnum;
import cc.geektip.geekoj.common.common.R;
import cc.geektip.geekoj.common.constant.UserConstant;
import cc.geektip.geekoj.common.exception.ThrowUtils;
import cc.geektip.geekoj.questionservice.utils.SessionUtils;
import cn.dev33.satoken.annotation.SaCheckRole;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
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
    @Resource
    private SessionUtils sessionUtils;

    /**
     * 创建
     *
     * @param questionAddRequest
     * @return
     */
    @PostMapping("/add")
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    public R<Long> addQuestion(@RequestBody @NotNull @Valid QuestionAddRequest questionAddRequest) {
        UserInfoVo currentUser = sessionUtils.getCurrentUser();
        Question question = new Question();
        BeanUtils.copyProperties(questionAddRequest, question);
        question.setUserId(currentUser.getUid());
        List<String> tags = questionAddRequest.getTags();
        question.setTags(JSON.toJSONString(tags));
        List<JudgeCase> judgeCase = questionAddRequest.getJudgeCase();
        question.setJudgeCase(JSON.toJSONString(judgeCase));
        JudgeConfig judgeConfig = questionAddRequest.getJudgeConfig();
        question.setJudgeConfig(JSON.toJSONString(judgeConfig));

        boolean ok = questionService.save(question);
        ThrowUtils.throwIf(!ok, AppHttpCodeEnum.INTERNAL_SERVER_ERROR);
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
        long pageNum = questionQueryRequest.getPageNum();
        long pageSize = questionQueryRequest.getPageSize();
        Page<Question> questionPage = questionService.page(new Page<>(pageNum, pageSize),
                questionService.getQueryWrapper(questionQueryRequest));
        List<QuestionVo> records = questionPage.getRecords().stream().map(QuestionVo::objToVo).toList();
        Page<QuestionVo> page = new Page<>(pageNum, pageSize, questionPage.getTotal());
        page.setRecords(records);
        return R.ok(page);
    }

    /**
     * 更新（仅管理员）
     * @param questionUpdateRequest
     * @return
     */
    @PutMapping
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    public R<Boolean> updateQuestion(@RequestBody @NotNull @Valid QuestionUpdateRequest questionUpdateRequest) {
        Question question = new Question();
        BeanUtils.copyProperties(questionUpdateRequest, question);
        //设置其他信息
        question.setTags(JSON.toJSONString(questionUpdateRequest.getTags()));
        question.setJudgeCase(JSON.toJSONString(questionUpdateRequest.getJudgeCase()));
        question.setJudgeConfig(JSON.toJSONString(questionUpdateRequest.getJudgeConfig()));

        long id = questionUpdateRequest.getId();
        // 判断是否存在
        Question oldQuestion = questionService.getById(id);
        ThrowUtils.throwIf(oldQuestion == null, AppHttpCodeEnum.NOT_EXIST);
        boolean result = questionService.updateById(question);
        return R.ok(result);
    }


    /**
     * 根据 id 获取完整信息（仅管理员，包含测试用例）
     * @param id
     * @return
     */
    @GetMapping("/{id}/vo")
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    public R<QuestionVo> getQuestionVoById(@PathVariable("id") @Min(1) Long id) {
        Question question = questionService.getById(id);
        ThrowUtils.throwIf(question == null, AppHttpCodeEnum.NOT_EXIST);
        QuestionVo questionVo = QuestionVo.objToVo(question);
        return R.ok(questionVo);
    }

    /**
     * 删除（仅管理员）
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    public R<Boolean> deleteQuestion(@PathVariable("id") @Min(1) Long id) {
        Question oldQuestion = questionService.getById(id);
        ThrowUtils.throwIf(oldQuestion == null, AppHttpCodeEnum.NOT_EXIST);
        boolean result = questionService.removeById(id);
        return R.ok(result);
    }

    /**
     * 获取所有标签
     * @return
     */
    @GetMapping("/tags")
    public R<List<String>> getQuestionTags() {
        List<String> tags = questionService.getQuestionTags();
        return R.ok(tags);
    }


    /**
     * 根据 id 获取（脱敏）
     * @param id
     * @return
     */
    @GetMapping("/{id}/vo/safe")
    public R<SafeQuestionVo> getSafeQuestionVoById(@PathVariable("id") @Min(1) Long id) {
        UserInfoVo currentUser = sessionUtils.getCurrentUser();
        Question question = questionService.getById(id);
        ThrowUtils.throwIf(question == null, AppHttpCodeEnum.NOT_EXIST);
        SafeQuestionVo vo = questionService.objToVo(question, currentUser.getUid());
        return R.ok(vo);
    }

    /**
     * 分页获取列表（封装类）
     * @param questionQueryRequest
     * @return
     */
    @PostMapping("/page/vo/safe")
    public R<Page<SafeQuestionVo>> listSafeQuestionVoByPage(@RequestBody QuestionQueryRequest questionQueryRequest) {
        long pageNum = questionQueryRequest.getPageNum();
        long pageSize = questionQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(pageSize > 20, AppHttpCodeEnum.PARAMS_ERROR);

        QueryWrapper<Question> queryWrapper = questionService.getQueryWrapper(questionQueryRequest);
        if(queryWrapper != null){
            Page<Question> questionPage = questionService.page(new Page<>(pageNum, pageSize), queryWrapper);

            UserInfoVo currentUser = sessionUtils.getCurrentUser();
            List<SafeQuestionVo> records = questionPage.getRecords().stream()
                    .map(question -> questionService.objToVo(question, currentUser.getUid()))
                    .toList();
            Page<SafeQuestionVo> page = new Page<>(pageNum, pageSize, questionPage.getTotal());
            page.setRecords(records);
            return R.ok(page);
        } else {
            Page<SafeQuestionVo> page = new Page<>(pageNum, pageSize, 0);
            return R.ok(page);
        }
    }

    /**
     * 编辑
     * @param questionEditRequest
     * @return
     */
    @PostMapping("/edit")
    @SaCheckRole(UserConstant.ADMIN_ROLE)
    public R editQuestion(@RequestBody @NotNull @Valid QuestionEditRequest questionEditRequest) {
        Question question = new Question();
        BeanUtils.copyProperties(questionEditRequest, question);

        long id = questionEditRequest.getId();
        Question oldQuestion = questionService.getById(id);
        ThrowUtils.throwIf(oldQuestion == null, AppHttpCodeEnum.NOT_EXIST);

        questionService.updateById(question);
        return R.ok();
    }
}
