package cc.geektip.geekoj.common.constant;

public interface SystemConstant {
    //发送验证码形式
    int PHONE_CODE = 0;
    int MAIL_CODE = 1;
    //用户名前缀
    String USERNAME_PREFIX = "geekoj_user_";
    //登录用户
    String LOGIN_USER = "login_user";
    //标签颜色
    String[] TAG_COLORS = new String[]{"pink", "red", "orange", "green", "cyan", "blue", "purple"};

    //登录态cookie的name
    String TOKEN = "TOKEN";

    //推荐用户的相似度阈值
    double RECOMMEND_THRESHOLD = 0.6;
    int RANDOM_RECOMMEND_BATCH_SIZE = 50;
    int RECOMMEND_SIZE = 8;

    //文章的一些属性
    int ARTICLE_STATUS_PUBLISHED = 1;
    int ARTICLE_STATUS_DRAFT = 0;
    int ARTICLE_PRIME = 1;
    int ARTICLE_COMMON = 0;
    int ARTICLE_GLOBAL_TOP = 1;

    //消息的一些属性（类型）
    int NOTIFICATION_LIKE = 1;
    int NOTIFICATION_COMMENT = 2;
    int NOTIFICATION_MSG = 3;
    int NOTIFICATION_NOTICE = 4;

}

