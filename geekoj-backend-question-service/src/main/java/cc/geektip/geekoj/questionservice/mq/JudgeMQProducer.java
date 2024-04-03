package cc.geektip.geekoj.questionservice.mq;

import cc.geektip.geekoj.api.model.dto.mq.JudgeMQMsg;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.stereotype.Component;

import static cc.geektip.geekoj.common.constant.MqConstant.TOPIC_JUDGE;


/**
 * @description: 判题消息生产者
 * @author: Fish
 *
 */
@Slf4j
@Component
public class JudgeMQProducer {
    @Resource
    private RocketMQTemplate rocketMQTemplate;

    /**
     * 发送判题消息
     *
     * @param questionSubmitId 提交ID
     */
    public void sendJudgeMessage(Long questionSubmitId) {
        log.info("发送MQ消息(判题) topic：{} submitId：{}", TOPIC_JUDGE, questionSubmitId);
        rocketMQTemplate.convertAndSend(TOPIC_JUDGE, new JudgeMQMsg(questionSubmitId));
    }
}
