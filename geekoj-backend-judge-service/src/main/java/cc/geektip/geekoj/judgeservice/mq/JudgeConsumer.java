package cc.geektip.geekoj.judgeservice.mq;

import cc.geektip.geekoj.api.service.JudgeService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import static cc.geektip.geekoj.common.constant.MqConstant.*;

/**
 * @description: 判题消息消费者
 * @author: Fish
 * @date: 2024/3/26
 */
@Slf4j
@Component
@RocketMQMessageListener(topic = TOPIC_JUDGE, consumerGroup = CONSUMER_GROUP_JUDGE, consumeMode = ConsumeMode.ORDERLY)
public class JudgeConsumer implements RocketMQListener<Long> {
    @Resource
    private JudgeService judgeService;
    @Override
    public void onMessage(Long questionSubmitId) {
        try {
            judgeService.doJudge(questionSubmitId);
            log.info("消费MQ消息，完成 topic：{} submitId：{}", TOPIC_JUDGE, questionSubmitId);
        } catch (Exception e) {
            log.error("消费MQ消息，失败 topic：{} submitId：{}", TOPIC_JUDGE, questionSubmitId);
            throw e;
        }
    }
}
