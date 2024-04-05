package cc.geektip.geekoj.common.constant;

public interface RedisConstant {
    /**
     * 验证码
     */
    String CODE_SMS_CACHE_PREFIX = "user:code:sms:";
    String MAIL_CODE_CACHE_PREFIX = "user:code:mail:";

    /**
     * 用户标签
     */
    String USER_TAGS_CATEGORY = "user:tags:category";
    String USER_TAGS_PREFIX = "user:tags:id:";

    /**
     * 用户关注
     */
    String USER_FOLLOWS_PREFIX = "user:follows:id:";
    String USER_FOLLOWS_COUNT_PREFIX = "user:follows:count:id:";
    String USER_FANS_PREFIX = "user:fans:id:";
    String USER_FANS_COUNT_PREFIX = "user:fans:count:id:";
    Integer USER_FOLLOWS_FANS_TTL = 4;
}
