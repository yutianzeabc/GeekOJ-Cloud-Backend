package cc.geektip.geekoj.userservice.mq;

import cc.geektip.geekoj.api.model.entity.user.Follow;
import cc.geektip.geekoj.common.constant.MqConstant;
import jakarta.annotation.Resource;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.stereotype.Component;

/**
 * @description: 关注消息生产者
 * @author: Fish
 *
 */
@Component
public class FollowMQProducer {
    @Resource
    private RocketMQTemplate rocketMQTemplate;

    /**
     * 发送关注消息
     *
     * @param follow 关注实体
     */
    public void sendFollowMessage(Follow follow) {
        rocketMQTemplate.convertAndSend(MqConstant.TOPIC_FOLLOW, follow);
    }
}
