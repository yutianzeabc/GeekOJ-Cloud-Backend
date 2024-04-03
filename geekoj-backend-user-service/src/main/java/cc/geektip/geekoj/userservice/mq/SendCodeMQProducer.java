package cc.geektip.geekoj.userservice.mq;

import cc.geektip.geekoj.api.model.dto.mq.SendCodeMsg;
import jakarta.annotation.Resource;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.stereotype.Component;

import static cc.geektip.geekoj.common.constant.SystemConstant.MAIL_CODE;
import static cc.geektip.geekoj.common.constant.SystemConstant.PHONE_CODE;

/**
@description: 验证码生产者
 * @author: Fish
 *
 */
@Component
public class SendCodeMQProducer {
    @Resource
    private RocketMQTemplate rocketMQTemplate;

    /**
     * 发送验证码消息
     *
     * @param type    类型
     * @param dest    目标
     * @param codeNum 验证码
     */
    public void sendCodeMessage(int type, String dest, String codeNum) {
        if (type == PHONE_CODE) {
            rocketMQTemplate.convertAndSend(new SendCodeMsg("phone", dest, codeNum));
        } else if (type == MAIL_CODE) {
            rocketMQTemplate.convertAndSend(new SendCodeMsg("mail", dest, codeNum));
        }
    }
}