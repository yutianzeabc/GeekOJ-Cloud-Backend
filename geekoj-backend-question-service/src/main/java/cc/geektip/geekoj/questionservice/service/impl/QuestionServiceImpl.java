package cc.geektip.geekoj.questionservice.service.impl;

import cc.geektip.geekoj.api.model.dto.judge.JudgeCase;
import cc.geektip.geekoj.api.model.dto.judge.JudgeConfig;
import cc.geektip.geekoj.api.model.dto.question.QuestionAddRequest;
import cc.geektip.geekoj.api.model.dto.question.QuestionEditRequest;
import cc.geektip.geekoj.api.model.dto.question.QuestionQueryRequest;
import cc.geektip.geekoj.api.model.dto.question.QuestionUpdateRequest;
import cc.geektip.geekoj.api.model.entity.question.Question;
import cc.geektip.geekoj.api.model.entity.question.QuestionSubmit;
import cc.geektip.geekoj.api.model.enums.question.QuestionSubmitStatusEnum;
import cc.geektip.geekoj.api.model.enums.question.UserQuestionStatusEnum;
import cc.geektip.geekoj.api.model.vo.question.QuestionVo;
import cc.geektip.geekoj.api.model.vo.question.SafeQuestionVo;
import cc.geektip.geekoj.api.model.vo.user.UserInfoVo;
import cc.geektip.geekoj.api.service.question.QuestionService;
import cc.geektip.geekoj.api.service.question.QuestionSubmitService;
import cc.geektip.geekoj.common.common.AppHttpCodeEnum;
import cc.geektip.geekoj.common.constant.CommonConstant;
import cc.geektip.geekoj.common.exception.ThrowUtils;
import cc.geektip.geekoj.common.utils.SqlUtils;
import cc.geektip.geekoj.questionservice.mapper.QuestionMapper;
import cc.geektip.geekoj.questionservice.utils.SessionUtils;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 题目服务实现
 */
@DubboService
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements QuestionService {
    @Resource
    @Lazy
    private QuestionSubmitService questionSubmitService;
    @Resource
    private SessionUtils sessionUtils;

    /**
     * 获取查询包装类
     *
     * @param questionQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<Question> getQueryWrapper(QuestionQueryRequest questionQueryRequest) {
        QueryWrapper<Question> queryWrapper = new QueryWrapper<>();

        String sortField = questionQueryRequest.getSortField();
        String sortOrder = questionQueryRequest.getSortOrder();
        String keyword = questionQueryRequest.getKeyword();
        String status = questionQueryRequest.getStatus();
        String difficulty = questionQueryRequest.getDifficulty();
        List<String> tags = questionQueryRequest.getTags();

        var questionSubmitMapper = questionSubmitService.getBaseMapper();
        // 忽略 content 和 answer
        queryWrapper.select(Question.class, item -> !item.getColumn().equals("content") && !item.getColumn().equals("answer"));

        UserQuestionStatusEnum statusEnum = UserQuestionStatusEnum.getEnumByText(status);
        if (statusEnum != null && !statusEnum.equals(UserQuestionStatusEnum.ALL)) {
            Long currentUid = sessionUtils.getCurrentUserId();
            Set<Long> passedIds;
            Set<Long> triedIds;

            switch (statusEnum) {
                case PASSED:
                    passedIds = questionSubmitMapper.selectList(new LambdaQueryWrapper<QuestionSubmit>()
                                    .select(QuestionSubmit::getQuestionId).eq(QuestionSubmit::getUserId, currentUid)
                                    .eq(QuestionSubmit::getStatus, QuestionSubmitStatusEnum.SUCCEED.getValue()))
                            .stream().map(QuestionSubmit::getQuestionId).collect(Collectors.toSet());
                    if (passedIds.isEmpty()) {
                        return null;
                    }
                    queryWrapper.in("id", passedIds);
                    break;
                case ATTEMPTED:
                    passedIds = questionSubmitMapper.selectList(new LambdaQueryWrapper<QuestionSubmit>()
                                    .select(QuestionSubmit::getQuestionId).eq(QuestionSubmit::getUserId, currentUid)
                                    .eq(QuestionSubmit::getStatus, QuestionSubmitStatusEnum.SUCCEED.getValue()))
                            .stream().map(QuestionSubmit::getQuestionId).collect(Collectors.toSet());
                    triedIds = questionSubmitMapper.selectList(new LambdaQueryWrapper<QuestionSubmit>()
                                    .select(QuestionSubmit::getQuestionId).eq(QuestionSubmit::getUserId, currentUid)
                                    .ne(QuestionSubmit::getStatus, QuestionSubmitStatusEnum.SUCCEED.getValue()))
                            .stream().map(QuestionSubmit::getQuestionId).collect(Collectors.toSet());
                    triedIds = (Set<Long>) CollUtil.subtract(triedIds, passedIds);
                    if (triedIds.isEmpty()) {
                        return null;
                    }
                    queryWrapper.in("id", triedIds);
                    break;
                case NOT_STARTED:
                    triedIds = questionSubmitMapper.selectList(new LambdaQueryWrapper<QuestionSubmit>()
                                    .select(QuestionSubmit::getQuestionId).eq(QuestionSubmit::getUserId, currentUid))
                            .stream().map(QuestionSubmit::getQuestionId).collect(Collectors.toSet());
                    if (!triedIds.isEmpty()) {
                        queryWrapper.notIn("id", triedIds);
                    }
                    break;
            }
        }

        // 拼接查询条件
        boolean likeQuery = StringUtils.isNotBlank(keyword);
        queryWrapper.and(likeQuery, wrapper -> wrapper.like("title", keyword));
        if (CollUtil.isNotEmpty(tags)) {
            for (String tag : tags) {
                queryWrapper.like("tags", "\"" + tag + "\"");
            }
        }
        queryWrapper.eq(StringUtils.isNotBlank(difficulty), "difficulty", difficulty);

        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        return queryWrapper;
    }

    @Override
    public SafeQuestionVo objToVo(Question question, UserInfoVo currentUser) {
        if (question == null) {
            return null;
        }
        SafeQuestionVo safeQuestionVO = new SafeQuestionVo();
        BeanUtils.copyProperties(question, safeQuestionVO);
        safeQuestionVO.setTags(JSONUtil.toList(question.getTags(), String.class));
        safeQuestionVO.setJudgeConfig(JSONUtil.toBean(question.getJudgeConfig(), JudgeConfig.class));

        if (currentUser == null) {
            return safeQuestionVO;
        }
        var questionSubmitMapper = questionSubmitService.getBaseMapper();
        //查询当前用户历史做题信息（已通过、尝试过、未开始）
        QuestionSubmit submit = questionSubmitMapper.selectOne(new QueryWrapper<QuestionSubmit>()
                .select("max(status) as status").lambda()
                .eq(QuestionSubmit::getQuestionId, question.getId())
                .eq(QuestionSubmit::getUserId, currentUser.getUid()));

        if (submit == null) {
            safeQuestionVO.setStatus(UserQuestionStatusEnum.NOT_STARTED.getText());
        } else if (submit.getStatus().equals(QuestionSubmitStatusEnum.SUCCEED.getValue())) {
            safeQuestionVO.setStatus(UserQuestionStatusEnum.PASSED.getText());
        } else if (submit.getStatus().equals(QuestionSubmitStatusEnum.FAILED.getValue())) {
            safeQuestionVO.setStatus(UserQuestionStatusEnum.ATTEMPTED.getText());
        } else {
            safeQuestionVO.setStatus(UserQuestionStatusEnum.NOT_STARTED.getText());
        }

        return safeQuestionVO;
    }

    @Override
    public String getQuestionTitleById(Long questionId) {
        return lambdaQuery().select(Question::getTitle)
                .eq(Question::getId, questionId)
                .oneOpt()
                .map(Question::getTitle)
                .orElse(null);
    }

    @Override
    public List<String> getQuestionTags() {
        return lambdaQuery().select(Question::getTags)
                .list()
                .stream()
                .flatMap(question -> JSONUtil.toList(question.getTags(), String.class).stream())
                .distinct()
                .toList();
    }

    @Override
    public Page<QuestionVo> listQuestionVoByPage(QuestionQueryRequest questionQueryRequest) {
        long pageNum = questionQueryRequest.getPageNum();
        long pageSize = questionQueryRequest.getPageSize();
        Page<Question> questionPage = page(new Page<>(pageNum, pageSize), getQueryWrapper(questionQueryRequest));
        List<QuestionVo> records = questionPage.getRecords().stream().map(QuestionVo::objToVo).toList();
        Page<QuestionVo> page = new Page<>(pageNum, pageSize, questionPage.getTotal());
        page.setRecords(records);
        return page;
    }

    @Override
    @Transactional
    public Question addQuestion(QuestionAddRequest questionAddRequest) {
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

        boolean ok = save(question);
        ThrowUtils.throwIf(!ok, AppHttpCodeEnum.INTERNAL_SERVER_ERROR);
        return question;
    }

    @Override
    @Transactional
    public boolean updateQuestion(QuestionUpdateRequest questionUpdateRequest) {
        Question question = new Question();
        BeanUtils.copyProperties(questionUpdateRequest, question);
        question.setTags(JSON.toJSONString(questionUpdateRequest.getTags()));
        question.setJudgeCase(JSON.toJSONString(questionUpdateRequest.getJudgeCase()));
        question.setJudgeConfig(JSON.toJSONString(questionUpdateRequest.getJudgeConfig()));

        long id = questionUpdateRequest.getId();
        Question oldQuestion = getById(id);
        ThrowUtils.throwIf(oldQuestion == null, AppHttpCodeEnum.NOT_EXIST);
        return updateById(question);
    }

    @Override
    public QuestionVo getQuestionVoById(Long id) {
        Question question = getById(id);
        ThrowUtils.throwIf(question == null, AppHttpCodeEnum.NOT_EXIST);
        return QuestionVo.objToVo(question);
    }

    @Override
    @Transactional
    public boolean deleteQuestion(Long id) {
        Question oldQuestion = getById(id);
        ThrowUtils.throwIf(oldQuestion == null, AppHttpCodeEnum.NOT_EXIST);
        return removeById(id);
    }

    @Override
    @Transactional
    public boolean editQuestion(QuestionEditRequest questionEditRequest) {
        Question question = new Question();
        BeanUtils.copyProperties(questionEditRequest, question);

        long id = questionEditRequest.getId();
        Question oldQuestion = getById(id);
        ThrowUtils.throwIf(oldQuestion == null, AppHttpCodeEnum.NOT_EXIST);
        return updateById(question);
    }

    @Override
    public SafeQuestionVo getSafeQuestionVoById(Long id) {
        UserInfoVo currentUser = sessionUtils.getCurrentUser();
        Question question = getById(id);
        ThrowUtils.throwIf(question == null, AppHttpCodeEnum.NOT_EXIST);
        return objToVo(question, currentUser);
    }

    @Override
    public Page<SafeQuestionVo> listSafeQuestionVoByPage(QuestionQueryRequest questionQueryRequest) {
        long pageNum = questionQueryRequest.getPageNum();
        long pageSize = questionQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(pageSize > 20, AppHttpCodeEnum.PARAMS_ERROR);

        QueryWrapper<Question> queryWrapper = getQueryWrapper(questionQueryRequest);
        Page<Question> questionPage = page(new Page<>(pageNum, pageSize), queryWrapper);
        UserInfoVo currentUser = sessionUtils.getCurrentUserPermitNull();
        List<SafeQuestionVo> records = questionPage.getRecords().stream()
                .map(question -> objToVo(question, currentUser))
                .toList();
        Page<SafeQuestionVo> page = new Page<>(pageNum, pageSize, questionPage.getTotal());
        page.setRecords(records);

        return page;
    }

    @Override
    public void incrQuestionSubmitCount(Long questionId) {
        baseMapper.incrSubmitCount(questionId);
    }

    @Override
    public void incrQuestionAcceptedCount(Long questionId) {
        baseMapper.incrAcceptedCount(questionId);
    }
}
