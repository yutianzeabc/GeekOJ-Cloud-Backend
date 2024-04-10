package cc.geektip.geekoj.questionservice.service.impl;

import cc.geektip.geekoj.api.model.dto.question.QuestionSubmitAddRequest;
import cc.geektip.geekoj.api.model.dto.question.QuestionSubmitQueryRequest;
import cc.geektip.geekoj.api.model.entity.question.Question;
import cc.geektip.geekoj.api.model.entity.question.QuestionSubmit;
import cc.geektip.geekoj.api.model.enums.question.LanguageEnum;
import cc.geektip.geekoj.api.model.enums.question.QuestionDifficultyEnum;
import cc.geektip.geekoj.api.model.enums.question.QuestionSubmitStatusEnum;
import cc.geektip.geekoj.api.model.vo.judge.JudgeInfo;
import cc.geektip.geekoj.api.model.vo.question.QuestionSubmitSummaryVo;
import cc.geektip.geekoj.api.model.vo.question.QuestionSubmitVo;
import cc.geektip.geekoj.api.model.vo.user.UserInfoVo;
import cc.geektip.geekoj.api.service.question.QuestionService;
import cc.geektip.geekoj.api.service.question.QuestionSubmitService;
import cc.geektip.geekoj.common.common.AppHttpCodeEnum;
import cc.geektip.geekoj.common.exception.ThrowUtils;
import cc.geektip.geekoj.questionservice.mapper.QuestionSubmitMapper;
import cc.geektip.geekoj.questionservice.mq.JudgeMQProducer;
import cc.geektip.geekoj.questionservice.utils.SessionUtils;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 题目提交服务实现
 */
@DubboService
public class QuestionSubmitServiceImpl extends ServiceImpl<QuestionSubmitMapper, QuestionSubmit> implements QuestionSubmitService {
    @Resource
    private SessionUtils sessionUtils;
    @Resource
    private QuestionService questionService;
    @Resource
    private JudgeMQProducer judgeMQProducer;

    /**
     * 提交题目
     *
     * @param questionSubmitAddRequest
     * @return
     */
    @Transactional
    @Override
    public QuestionSubmit doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest) {
        // 校验编程语言是否合法
        String language = questionSubmitAddRequest.getLanguage();
        LanguageEnum languageEnum = LanguageEnum.getEnumByValue(language);
        ThrowUtils.throwIf(languageEnum == null, AppHttpCodeEnum.PARAMS_ERROR, "不支持的编程语言");
        long questionId = questionSubmitAddRequest.getQuestionId();

        // 判断实体是否存在，根据类别获取实体
        Question question = questionService.getById(questionId);
        ThrowUtils.throwIf(question == null, AppHttpCodeEnum.NOT_EXIST, "题目不存在");

        // 取得当前登录用户
        Long userId = sessionUtils.getCurrentUserId();

        // 判断用户是否有正在等待或判题的题，如果有，提交判题失败
        QuestionSubmit submit = lambdaQuery().eq(QuestionSubmit::getUserId, userId)
                .and(wrapper -> wrapper.eq(QuestionSubmit::getStatus, QuestionSubmitStatusEnum.WAITING)
                        .or()
                        .eq(QuestionSubmit::getStatus, QuestionSubmitStatusEnum.RUNNING))
                .one();
        ThrowUtils.throwIf(submit != null, AppHttpCodeEnum.SUBMIT_ERROR, "提交过于频繁");

        // 提交数+1
        questionService.incrQuestionSubmitCount(questionId);

        // 提交题目
        QuestionSubmit questionSubmit = new QuestionSubmit();
        questionSubmit.setUserId(userId);
        questionSubmit.setQuestionId(questionId);
        questionSubmit.setCode(questionSubmitAddRequest.getCode());
        questionSubmit.setLanguage(language);

        // 设置初始状态
        questionSubmit.setStatus(QuestionSubmitStatusEnum.WAITING.getValue());
        questionSubmit.setJudgeInfo("{}");
        boolean ok = this.save(questionSubmit);
        ThrowUtils.throwIf(!ok, AppHttpCodeEnum.INTERNAL_SERVER_ERROR, "数据插入失败");
        Long questionSubmitId = questionSubmit.getId();

        // 发送判题消息
        judgeMQProducer.sendJudgeMessage(questionSubmitId);

        return questionSubmit;
    }


    @Override
    public Page<QuestionSubmitVo> listMyQuestionSubmitVoByPage(QuestionSubmitQueryRequest queryRequest) {
        // 取得当前登录用户
        UserInfoVo currentUser = sessionUtils.getCurrentUser();
        // 获取分页信息
        long pageNum = queryRequest.getPageNum();
        long pageSize = queryRequest.getPageSize();
        Long questionId = queryRequest.getQuestionId();
        Page<QuestionSubmit> page = lambdaQuery().select(QuestionSubmit.class, item -> !item.getColumn().equals("code"))
                .eq(QuestionSubmit::getUserId, currentUser.getUid())
                .eq(queryRequest.getQuestionId() != null, QuestionSubmit::getQuestionId, questionId)
                .orderByDesc(QuestionSubmit::getCreateTime)
                .page(new Page<>(pageNum, pageSize));
        List<QuestionSubmitVo> records = page.getRecords().parallelStream().map(submit -> objToVo(submit, currentUser)).toList();
        Page<QuestionSubmitVo> voPage = new Page<>(pageNum, pageSize, page.getTotal());
        voPage.setRecords(records);
        return voPage;
    }

    @Override
    public Page<QuestionSubmitVo> listAllQuestionSubmitVoByPage(QuestionSubmitQueryRequest queryRequest) {
        // 获取当前登录用户
        UserInfoVo currentUser = sessionUtils.getCurrentUserPermitNull();
        // 获取分页信息
        long pageNum = queryRequest.getPageNum();
        long pageSize = queryRequest.getPageSize();
        Long questionId = queryRequest.getQuestionId();
        Page<QuestionSubmit> page = lambdaQuery().select(QuestionSubmit.class, item -> !item.getColumn().equals("code"))
                .eq(queryRequest.getQuestionId() != null, QuestionSubmit::getQuestionId, questionId)
                .orderByDesc(QuestionSubmit::getCreateTime)
                .page(new Page<>(pageNum, pageSize));
        List<QuestionSubmitVo> records = page.getRecords().parallelStream().map(submit -> objToVo(submit, currentUser)).toList();
        Page<QuestionSubmitVo> voPage = new Page<>(pageNum, pageSize, page.getTotal());
        voPage.setRecords(records);
        return voPage;
    }

    @Override
    public QuestionSubmitVo getQuestionSubmitVoById(Long id) {
        // 获取当前登录用户
        UserInfoVo currentUser = sessionUtils.getCurrentUser();
        QuestionSubmit submit = getById(id);
        ThrowUtils.throwIf(submit == null, AppHttpCodeEnum.NOT_EXIST, "提交记录不存在");
        return objToVo(submit, currentUser);
    }

    @Override
    public QuestionSubmitSummaryVo getSubmitSummary() {
        // 获取当前登录用户
        UserInfoVo currentUser = sessionUtils.getCurrentUserPermitNull();

        QuestionSubmitSummaryVo summaryVo = new QuestionSubmitSummaryVo();
        // 获取简单、中等、困难题目
        List<Long> easyIds = questionService.lambdaQuery().select(Question::getId)
                .eq(Question::getDifficulty, QuestionDifficultyEnum.EASY.getValue()).list().stream().map(Question::getId).toList();
        List<Long> mediumIds = questionService.lambdaQuery().select(Question::getId)
                .eq(Question::getDifficulty, QuestionDifficultyEnum.MEDIUM.getValue()).list().stream().map(Question::getId).toList();
        List<Long> hardIds = questionService.lambdaQuery().select(Question::getId)
                .eq(Question::getDifficulty, QuestionDifficultyEnum.HARD.getValue()).list().stream().map(Question::getId).toList();
        long easyTotal = easyIds.size();
        long mediumTotal = mediumIds.size();
        long hardTotal = hardIds.size();
        summaryVo.setEasyTotal(easyTotal);
        summaryVo.setMediumTotal(mediumTotal);
        summaryVo.setHardTotal(hardTotal);
        summaryVo.setTotal(easyTotal + mediumTotal + hardTotal);

        if (currentUser == null) {
            summaryVo.setEasyPass(0L);
            summaryVo.setMediumPass(0L);
            summaryVo.setHardPass(0L);
            summaryVo.setSubmitCount(0L);
            summaryVo.setPassCount(0L);
            return summaryVo;
        }
        long userId = currentUser.getUid();

        // 获取用户通过的简单、中等、困难题目数
        Long easyPass = getBaseMapper().getPassCount(userId, easyIds);
        Long mediumPass = getBaseMapper().getPassCount(userId, mediumIds);
        Long hardPass = getBaseMapper().getPassCount(userId, hardIds);
        summaryVo.setEasyPass(easyPass);
        summaryVo.setMediumPass(mediumPass);
        summaryVo.setHardPass(hardPass);

        //获取用户提交总数
        Long submitCount = lambdaQuery().eq(QuestionSubmit::getUserId, userId).count();
        summaryVo.setSubmitCount(submitCount);
        //获取用户成功的提交
        Long passCount = lambdaQuery().eq(QuestionSubmit::getUserId, userId)
                .eq(QuestionSubmit::getStatus, QuestionSubmitStatusEnum.SUCCEED.getValue()).count();
        summaryVo.setPassCount(passCount);

        return summaryVo;
    }

    /**
     * 对象转包装类
     *
     * @param questionSubmit
     * @return
     */
    public QuestionSubmitVo objToVo(QuestionSubmit questionSubmit, UserInfoVo currentUser) {
        if (questionSubmit == null) {
            return null;
        }
        QuestionSubmitVo questionSubmitVO = new QuestionSubmitVo();
        BeanUtils.copyProperties(questionSubmit, questionSubmitVO);
        questionSubmitVO.setQuestionTitle(questionService.getQuestionTitleById(questionSubmit.getQuestionId()));
        questionSubmitVO.setJudgeInfo(JSONUtil.toBean(questionSubmit.getJudgeInfo(), JudgeInfo.class));
        // 如果当前用户没有查看提交记录的权限，隐藏敏感信息
        if (!sessionUtils.hasUserView(currentUser, questionSubmitVO.getUserId())) {
            questionSubmitVO.getJudgeInfo().setMessage(null);
            questionSubmitVO.getJudgeInfo().setInput(null);
            questionSubmitVO.getJudgeInfo().setOutput(null);
            questionSubmitVO.getJudgeInfo().setExpectedOutput(null);
            questionSubmitVO.setCode(null);
        }
        return questionSubmitVO;
    }
}







