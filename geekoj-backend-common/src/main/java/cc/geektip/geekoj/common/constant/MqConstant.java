package cc.geektip.geekoj.common.constant;

/**
 * @description: 消息队列常量类
 * @author: Fish
 *
 */
public interface MqConstant {
    /**
     * MQ主题： 判题
     */
    String TOPIC_JUDGE = "geekoj_judge";
    String CONSUMER_GROUP_JUDGE = "geekoj_judge";
    /**
     * MQ主题： 验证码
     */
    String TOPIC_CODE = "geekoj_code";
    String CONSUMER_GROUP_CODE = "geekoj_code";
    /**
     * MQ主题： 关注
     */
    String TOPIC_FOLLOW = "geekoj_follow";
    String CONSUMER_GROUP_FOLLOW = "geekoj_follow";
}
