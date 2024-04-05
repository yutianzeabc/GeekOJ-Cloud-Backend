package cc.geektip.geekoj.userservice.mq;

import cc.geektip.geekoj.api.model.entity.user.Follow;
import cc.geektip.geekoj.api.service.user.FollowService;
import cc.geektip.geekoj.api.service.user.UserService;
import cc.geektip.geekoj.common.constant.RedisConstant;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

import static cc.geektip.geekoj.common.constant.MqConstant.CONSUMER_GROUP_FOLLOW;
import static cc.geektip.geekoj.common.constant.MqConstant.TOPIC_FOLLOW;

/**
 * @description: 关注消息消费者
 * @author: Fish
 *
 */
@Slf4j
@Component
@RocketMQMessageListener(topic = TOPIC_FOLLOW, consumerGroup = CONSUMER_GROUP_FOLLOW, maxReconsumeTimes = 2)
public class FollowMQConsumer implements RocketMQListener<Follow> {
    @Resource
    private RedisTemplate<String, Long> redisTemplate;
    @Resource
    private FollowService followService;
    @Resource
    private UserService userService;

    @Override
    @Transactional
    public void onMessage(Follow follow) {
        // 1. 获取当前是否已关注
        boolean isFollow = followService.isFollow(follow.getUid(), follow.getFollowUid());
        String fromUserFollowsKey = RedisConstant.USER_FOLLOWS_PREFIX + follow.getUid();
        String toUserFansKey = RedisConstant.USER_FANS_PREFIX + follow.getFollowUid();
        if (isFollow) {
            // 2.1 如果已关注，取消关注
            followService.removeByUidPair(follow.getUid(), follow.getFollowUid());
            userService.decrFollowsCount(follow.getUid());
            userService.decrFansCount(follow.getFollowUid());
            if (redisTemplate.hasKey(fromUserFollowsKey)) {
                redisTemplate.opsForSet().remove(fromUserFollowsKey, follow.getFollowUid());
                redisTemplate.expire(fromUserFollowsKey, RedisConstant.USER_FOLLOWS_FANS_TTL, TimeUnit.HOURS);
            }
            if (redisTemplate.hasKey(toUserFansKey)) {
                redisTemplate.opsForSet().remove(toUserFansKey, follow.getUid());
                redisTemplate.expire(toUserFansKey, RedisConstant.USER_FOLLOWS_FANS_TTL, TimeUnit.HOURS);
            }

            log.info("取消关注成功，uid：{} followUid：{}", follow.getUid(), follow.getFollowUid());
        } else {
            // 2.2 如果未关注，添加关注
            followService.save(follow);
            userService.incrFollowsCount(follow.getUid());
            userService.incrFansCount(follow.getFollowUid());
            if (redisTemplate.hasKey(fromUserFollowsKey)) {
                redisTemplate.opsForSet().add(fromUserFollowsKey, follow.getFollowUid());
                redisTemplate.expire(fromUserFollowsKey, RedisConstant.USER_FOLLOWS_FANS_TTL, TimeUnit.HOURS);
            }
            if (redisTemplate.hasKey(toUserFansKey)) {
                redisTemplate.opsForSet().add(toUserFansKey, follow.getUid());
                redisTemplate.expire(toUserFansKey, RedisConstant.USER_FOLLOWS_FANS_TTL, TimeUnit.HOURS);
            }
            log.info("关注成功，uid：{} followUid：{}", follow.getUid(), follow.getFollowUid());
        }
    }
}
