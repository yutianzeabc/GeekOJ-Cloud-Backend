package cc.geektip.geekoj.questionservice.mq;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.stereotype.Component;

import static cc.geektip.geekoj.common.constant.MqConstant.*;


/**
 * @description: 判题消息生产者
 * @author: Fish
 * @date: 2024/3/26
 */
@Slf4j
@Component
public class JudgeProducer {
    @Resource
    private RocketMQTemplate rocketMQTemplate;

    /**
     * 发送判题消息
     *
     * @param questionSubmitId 提交ID
     */
    public void sendJudgeMessage(Long questionSubmitId) {
        log.info("发送MQ消息(判题) topic：{} submitId：{}", TOPIC_JUDGE, questionSubmitId);
        rocketMQTemplate.asyncSendOrderly(TOPIC_JUDGE, questionSubmitId, String.valueOf(questionSubmitId), new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                if (SendStatus.SEND_OK.equals(sendResult.getSendStatus())) {
                    log.info("发送MQ消息(判题)成功 topic：{} submitId：{}", TOPIC_JUDGE, questionSubmitId);
                } else {
                    log.warn("发送MQ消息(判题)失败 topic：{} submitId：{}", TOPIC_JUDGE, questionSubmitId);
                }
            }

            @Override
            public void onException(Throwable throwable) {
                log.error("发送MQ消息(判题)失败 topic：{} submitId：{}", TOPIC_JUDGE, questionSubmitId, throwable);
            }
        });
    }
}
