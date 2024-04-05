package cc.geektip.geekoj.questionservice.service.impl;

import cc.geektip.geekoj.api.model.dto.judge.JudgeConfig;
import cc.geektip.geekoj.api.model.dto.question.QuestionQueryRequest;
import cc.geektip.geekoj.api.model.entity.question.Question;
import cc.geektip.geekoj.api.model.entity.question.QuestionSubmit;
import cc.geektip.geekoj.api.model.enums.question.QuestionSubmitStatusEnum;
import cc.geektip.geekoj.api.model.enums.question.UserQuestionStatusEnum;
import cc.geektip.geekoj.api.model.vo.question.SafeQuestionVo;
import cc.geektip.geekoj.api.service.question.QuestionService;
import cc.geektip.geekoj.api.service.question.QuestionSubmitService;
import cc.geektip.geekoj.common.constant.CommonConstant;
import cc.geektip.geekoj.common.utils.SqlUtils;
import cc.geektip.geekoj.questionservice.mapper.QuestionMapper;
import cc.geektip.geekoj.questionservice.utils.SessionUtils;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;

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
        queryWrapper.like(likeQuery, "title", keyword);
        queryWrapper.like(likeQuery, "content", keyword);
        queryWrapper.like(likeQuery, "answer", keyword);
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
    public SafeQuestionVo objToVo(Question question, Long uid) {
        if (question == null) {
            return null;
        }
        SafeQuestionVo safeQuestionVO = new SafeQuestionVo();
        BeanUtils.copyProperties(question, safeQuestionVO);
        safeQuestionVO.setTags(JSONUtil.toList(question.getTags(), String.class));
        safeQuestionVO.setJudgeConfig(JSONUtil.toBean(question.getJudgeConfig(), JudgeConfig.class));

        var questionSubmitMapper = questionSubmitService.getBaseMapper();
        //查询当前用户历史做题信息（已通过、尝试过、未开始）
        QuestionSubmit submit = questionSubmitMapper.selectOne(new QueryWrapper<QuestionSubmit>()
                .select("max(status) as status").lambda()
                .eq(QuestionSubmit::getQuestionId, question.getId())
                .eq(QuestionSubmit::getUserId, uid));

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
    public List<String> getQuestionTags() {
        return lambdaQuery().select(Question::getTags)
                .list()
                .stream()
                .flatMap(question -> JSONUtil.toList(question.getTags(), String.class).stream())
                .distinct()
                .toList();
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
