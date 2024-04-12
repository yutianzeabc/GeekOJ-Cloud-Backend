package cc.geektip.geekoj.judgeservice.mq;

import cc.geektip.geekoj.api.model.dto.mq.JudgeMQMsg;
import cc.geektip.geekoj.api.service.judge.JudgeService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import static cc.geektip.geekoj.common.constant.MqConstant.CONSUMER_GROUP_JUDGE;
import static cc.geektip.geekoj.common.constant.MqConstant.TOPIC_JUDGE;

/**
 * @description: 判题消息消费者
 * @author: Bill Yu
 *
 */
@Slf4j
@Component
@RocketMQMessageListener(topic = TOPIC_JUDGE, consumerGroup = CONSUMER_GROUP_JUDGE, consumeMode = ConsumeMode.ORDERLY, maxReconsumeTimes = 3)
public class JudgeMQConsumer implements RocketMQListener<JudgeMQMsg> {
    @Resource
    private JudgeService judgeService;
    @Override
    public void onMessage(JudgeMQMsg judgeMQMsg) {
        Long questionSubmitId = judgeMQMsg.getQuestionSubmitId();
        try {
            judgeService.doJudge(questionSubmitId);
            log.debug("消费MQ消息，完成 topic：{} submitId：{}", TOPIC_JUDGE, questionSubmitId);
        } catch (Exception e) {
            log.error("消费MQ消息，失败 topic：{} submitId：{}", TOPIC_JUDGE, questionSubmitId);
            throw e;
        }
    }
}
