package cc.geektip.geekoj.userservice.mq;

import cc.geektip.geekoj.api.model.dto.mq.SendCodeMsg;
import cc.geektip.geekoj.userservice.utils.MailUtils;
import cc.geektip.geekoj.userservice.utils.SmsUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import static cc.geektip.geekoj.common.constant.MqConstant.CONSUMER_GROUP_CODE;
import static cc.geektip.geekoj.common.constant.MqConstant.TOPIC_CODE;

/**
 * @description: 验证码消息消费者
 * @author: Fish
 *
 */
@Slf4j
@Component
@RocketMQMessageListener(topic = TOPIC_CODE, consumerGroup = CONSUMER_GROUP_CODE, maxReconsumeTimes = 2)
public class SendCodeMQConsumer implements RocketMQListener<SendCodeMsg> {
    @Resource
    private SmsUtils smsUtils;
    @Resource
    private MailUtils mailUtil;

    @Override
    public void onMessage(SendCodeMsg sendCodeMsg) {
        String type = sendCodeMsg.getType();
        String dest = sendCodeMsg.getDest();
        String codeNum = sendCodeMsg.getCodeNum();

        try {
            if ("phone".equals(type)) {
                smsUtils.sendCode(dest, codeNum);
            } else if ("mail".equals(type)) {
                mailUtil.sendMail(dest, codeNum);
            }
            log.info("消费MQ消息，完成 topic：{} dest：{} code：{}", TOPIC_CODE, dest, codeNum);
        } catch (Exception e) {
            log.error("消费MQ消息，失败 topic：{} dest：{} code：{}", TOPIC_CODE, dest, codeNum);
            throw e;
        }
    }
}
