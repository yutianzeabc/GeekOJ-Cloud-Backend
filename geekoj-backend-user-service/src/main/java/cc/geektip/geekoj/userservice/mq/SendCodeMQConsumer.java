package cc.geektip.geekoj.userservice.mq;

import cc.geektip.geekoj.api.model.dto.mq.SendCodeMsg;
import cc.geektip.geekoj.userservice.utils.MailUtil;
import cc.geektip.geekoj.userservice.utils.SmsUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;

import static cc.geektip.geekoj.common.constant.MqConstant.CONSUMER_GROUP_CODE;
import static cc.geektip.geekoj.common.constant.MqConstant.TOPIC_CODE;

/**
 * @description: 验证码消息消费者
 * @author: Fish
 *
 */
@Slf4j
@Component
@RocketMQMessageListener(topic = TOPIC_CODE, consumerGroup = CONSUMER_GROUP_CODE, consumeMode = ConsumeMode.ORDERLY)
public class SendCodeMQConsumer implements RocketMQListener<SendCodeMsg> {
    @Resource
    private SmsUtil smsUtil;
    @Resource
    private MailUtil mailUtil;

    @Override
    public void onMessage(SendCodeMsg sendCodeMsg) {
        String type = sendCodeMsg.getType();
        String dest = sendCodeMsg.getDest();
        String codeNum = sendCodeMsg.getCodeNum();

        try {
            if ("phone".equals(type)) {
                smsUtil.sendCode(dest, codeNum);
            } else if ("mail".equals(type)) {
                mailUtil.sendMail(dest, codeNum);
            }
            log.info("消费MQ消息，完成 topic：{} dest：{}", TOPIC_CODE, dest);
        } catch (Exception e) {
            log.error("消费MQ消息，失败 topic：{} dest：{}", TOPIC_CODE, dest);
            throw e;
        }
    }
}
