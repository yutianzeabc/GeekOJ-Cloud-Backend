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

    /**
     * 用户信息
     */
    String USER_INFO_PREFIX = "user:info:id:";
    Integer USER_INFO_TTL = 1;

    /**
     * 用户推荐
     */
    String USER_RECOMMEND_PREFIX = "user:recommend:id:";

    /**
     * 文章标签
     */
    String ARTICLE_TAGS_CATEGORY = "article:tags:category";
    String ARTICLE_TAGS_PREFIX = "article:tags:id:";

    /**
     * 文章
     */
    Integer ARTICLE_TTL = 4;

    String ARTICLE_COVER_PREFIX = "article:id:";
    String ARTICLE_COVER_SUFFIX = ":cover";

    String ARTICLE_CONTENT_PREFIX = "article:id:";
    String ARTICLE_CONTENT_SUFFIX = ":content";

    //浏览量
    String ARTICLE_VIEW_PREFIX = "article:id:";
    String ARTICLE_VIEW_SUFFIX = ":view";
    //评论量
    String ARTICLE_COMMENT_PREFIX = "article:id:";
    String ARTICLE_COMMENT_SUFFIX = ":comment";

    String ARTICLE_LIKE_PREFIX = "article:id:";
    String ARTICLE_LIKE_SUFFIX = ":like";

    String ARTICLE_STAR_PREFIX = "article:id:";
    String ARTICLE_STAR_SUFFIX = ":star";

    /**
     * 通知
     */
    String NOTIFICATION_PREFIX = "notification:uid:";
    //点赞
    String LIKE_NOTIFICATION_SUFFIX = ":like";
    //评论
    String COMMENT_NOTIFICATION_SUFFIX = ":comment";
    //消息
    String MSG_NOTIFICATION_SUFFIX = ":msg";
    //系统通知
    String NOTICE_NOTIFICATION_SUFFIX = ":notice";


    String ARTICLE_PUBLISHED = "article:published";


    /**
     * 同步浏览量的锁
     */
    String ASYNC_COUNT_LOCK = "article:lock:count";
    /**
     * 更新文章score和hot的锁
     */
    String ASYNC_SCORE_LOCK = "article:lock:score";

    /**
     * 文章热榜
     */
    String HOT_ARTICLES = "article:hot";

    /**
     * 全局置顶
     */
    String GLOBAL_TOP = "article:top";
}
