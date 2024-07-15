package cc.geektip.geekoj.aiservice.service;

import cc.geektip.geekoj.aiservice.client.AiChatClient;
import cc.geektip.geekoj.aiservice.config.AiConfig;
import cc.geektip.geekoj.aiservice.utils.SessionUtils;
import cc.geektip.geekoj.api.model.dto.ai.AiAnalyseRequest;
import cc.geektip.geekoj.api.model.dto.ai.AiAnalyseResponse;
import cc.geektip.geekoj.api.model.entity.question.Question;
import cc.geektip.geekoj.api.model.entity.question.QuestionSubmit;
import cc.geektip.geekoj.api.model.vo.user.UserInfoVo;
import cc.geektip.geekoj.api.service.ai.AiService;
import cc.geektip.geekoj.api.service.question.QuestionService;
import cc.geektip.geekoj.api.service.question.QuestionSubmitService;
import cc.geektip.geekoj.common.common.AppHttpCodeEnum;
import cc.geektip.geekoj.common.exception.ThrowUtils;
import cn.hutool.core.util.StrUtil;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

import static cc.geektip.geekoj.common.constant.RedisConstant.AI_ANALYSE_PREFIX;
import static cc.geektip.geekoj.common.constant.RedisConstant.AI_USER_REQUEST_COUNT_PREFIX;

/**
 * @description: AI服务实现类
 * @author: Fish
 */
@Service
public class AiServiceImpl implements AiService {

    private static final String AI_ANALYSE_SYSTEM_PROMPT = """
            你是一位严谨的算法编程指导教师，我会给你如下信息：
            ```
            题目名称，
            【【题目描述】】，
            【【用户作答代码】】，
            【【代码沙箱测试输出】】
            ```
            请你根据上述信息，按照以下步骤来对用户作答进行分析：
            1. 要求：首先，分析用户代码的总体思路是否正确；
                    其次，分析用户算法的时间复杂度和空间复杂度是否为本题最优解；
                    最后，如果有可能存在的BUG，简要逐点列出错误点并给出相应的修复提示，无需给出修正后的代码片段。
            2. 输出语言要求：中文，表达严谨专业，内容精炼简要。
            3. 返回格式必须为Markdown格式文本，无额外的前后缀符号。""";

    private final Cache<String, String> aiAnswerCacheMap;
    @DubboReference
    private QuestionService questionService;
    @DubboReference
    private QuestionSubmitService questionSubmitService;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private AiChatClient aiChatClient;
    @Resource
    private AiConfig aiConfig;
    @Resource
    private SessionUtils sessionUtils;

    public AiServiceImpl() {
        aiAnswerCacheMap = Caffeine.newBuilder()
                .maximumSize(512)
                .expireAfterAccess(15, TimeUnit.MINUTES)
                .build();
    }

    @Override
    public AiAnalyseResponse doAnalyse(AiAnalyseRequest aiAnalyseRequest) {
        // 校验参数
        UserInfoVo currentUser = sessionUtils.getCurrentUser();

        Long questionSubmitId = aiAnalyseRequest.getQuestionSubmitId();
        QuestionSubmit questionSubmit = questionSubmitService.getById(questionSubmitId);
        ThrowUtils.throwIf(questionSubmit == null, AppHttpCodeEnum.NOT_EXIST, "提交记录不存在");
        ThrowUtils.throwIf(!sessionUtils.hasUserView(currentUser, questionSubmit.getUserId()), AppHttpCodeEnum.NO_AUTH, "当前无权分析该提交记录");

        // 查询本地缓存
        Long questionId = questionSubmit.getQuestionId();
        String cacheKey = buildCacheKey(questionId, questionSubmitId);
        String aiAnswer = aiAnswerCacheMap.getIfPresent(cacheKey);
        if (StrUtil.isNotBlank(aiAnswer)) {
            return new AiAnalyseResponse(aiAnswer);
        }
        // 查询Redis缓存
        aiAnswer = stringRedisTemplate.opsForValue().getAndExpire(cacheKey, 6, TimeUnit.HOURS);
        if (StrUtil.isNotBlank(aiAnswer)) {
            aiAnswerCacheMap.put(cacheKey, aiAnswer);
            return new AiAnalyseResponse(aiAnswer);
        }
        // 如果均查不到，调用AI服务，写入本地和Redis缓存
        // 检查请求次数限制
        checkRequestLimit(currentUser.getUid());
        // 查询题目
        Question question = questionService.getById(questionId);
        ThrowUtils.throwIf(question == null, AppHttpCodeEnum.NOT_EXIST, "题目不存在");
        // 调用AI服务
        String userPrompt = getAiAnalyseUserPrompt(question, questionSubmit);
        aiAnswer = aiChatClient.doSyncStableRequest(AI_ANALYSE_SYSTEM_PROMPT, userPrompt);
        // 写入本地缓存和Redis缓存
        aiAnswerCacheMap.put(cacheKey, aiAnswer);
        stringRedisTemplate.opsForValue().set(cacheKey, aiAnswer, 6, TimeUnit.HOURS);
        // 记录请求次数
        incrementRequestCount(currentUser.getUid());

        return new AiAnalyseResponse(aiAnswer);
    }

    private String getAiAnalyseUserPrompt(Question question, QuestionSubmit questionSubmit) {
        return question.getTitle() +
                ",\n" +
                "【【" +
                question.getContent() +
                "】】,\n" +
                "【【" +
                questionSubmit.getCode() +
                "】】,\n" +
                "【【" +
                questionSubmit.getJudgeInfo() +
                "】】";
    }


    private String buildCacheKey(Long questionId, Long questionSubmitId) {
        return AI_ANALYSE_PREFIX + questionId + ":" + questionSubmitId;
    }

    private void checkRequestLimit(Long userId) {
        String requestKey = buildRequestCountKey(userId);
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        String requestCountStr = ops.get(requestKey);
        int requestCount = requestCountStr == null ? 0 : Integer.parseInt(requestCountStr);
        ThrowUtils.throwIf(requestCount >= aiConfig.getUserDailyLimit(), AppHttpCodeEnum.RATE_LIMIT, "请求AI次数已达今日上限");
    }

    private void incrementRequestCount(Long userId) {
        String requestKey = buildRequestCountKey(userId);
        stringRedisTemplate.opsForValue().increment(requestKey, 1);
        // 设置计数器过期时间为1天
        stringRedisTemplate.expire(requestKey, 1, TimeUnit.DAYS);
    }

    private String buildRequestCountKey(Long userId) {
        return AI_USER_REQUEST_COUNT_PREFIX + userId;
    }

}
