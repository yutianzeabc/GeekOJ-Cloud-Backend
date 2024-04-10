package cc.geektip.geekoj.userservice.mq;

import cc.geektip.geekoj.api.model.entity.user.Follow;
import jakarta.annotation.Resource;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.stereotype.Component;

import static cc.geektip.geekoj.common.constant.MqConstant.TOPIC_FOLLOW;

/**
 * @description: 关注消息生产者
 * @author: Bill Yu
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
        rocketMQTemplate.convertAndSend(TOPIC_FOLLOW, follow);
    }
}
