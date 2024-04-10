DROP DATABASE IF EXISTS geekoj_cloud;
CREATE DATABASE IF NOT EXISTS geekoj_cloud;
USE geekoj_cloud;

CREATE TABLE `user`
(
    `uid`          bigint      NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `username`     varchar(64)          DEFAULT NULL COMMENT '用户名',
    `password`     varchar(64)          DEFAULT NULL COMMENT '密码',
    `user_role`    varchar(64) NOT NULL DEFAULT 'user' COMMENT '用户权限',
    `access_key`   varchar(512)         DEFAULT NULL,
    `secret_key`   varchar(512)         DEFAULT NULL,
    `tags`         varchar(512)         DEFAULT '[]' COMMENT '用户标签',
    `signature`    varchar(256)         DEFAULT '这个人很懒，什么都没写。' COMMENT '个性签名',
    `email`        varchar(64)          DEFAULT NULL COMMENT '邮箱',
    `phone`        varchar(32)          DEFAULT NULL COMMENT '手机号',
    `sex`          tinyint(1)  NOT NULL DEFAULT '1' COMMENT '性别',
    `avatar`       varchar(256)         DEFAULT 'https://gw.alipayobjects.com/zos/antfincdn/XAosXuNZyF/BiazfanxmamNRoxxVxka.png' COMMENT '头像',
    `follow`       int                  DEFAULT '0' COMMENT '关注',
    `fans`         int                  DEFAULT '0' COMMENT '粉丝',
    `social_uid`   varchar(256)         DEFAULT NULL COMMENT '第三方id',
    `access_token` varchar(256)         DEFAULT NULL COMMENT '第三方Token',
    `expires_in`   int                  DEFAULT NULL COMMENT '访问令牌的有效期',
    `create_time`  datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`  datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_delete`    tinyint(1)  NOT NULL DEFAULT '0' COMMENT '是否删除',
    PRIMARY KEY (`uid`),
    UNIQUE KEY `idx_username` (`username`),
    UNIQUE KEY `idx_email` (`email`),
    UNIQUE KEY `idx_phone` (`phone`),
    UNIQUE KEY `idx_social_uid` (`social_uid`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1000
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='用户';

CREATE TABLE `user_tag`
(
    `id`          bigint     NOT NULL AUTO_INCREMENT COMMENT '标签ID',
    `parent_id`   bigint     NOT NULL COMMENT '类别ID',
    `name`        varchar(64)         DEFAULT NULL COMMENT '标签名',
    `color`       varchar(8)          DEFAULT NULL COMMENT '颜色',
    `created_by`  bigint     NOT NULL COMMENT '创建用户',
    `create_time` datetime   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_delete`   tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标志',
    PRIMARY KEY (`id`),
    KEY `idx_parent_id` (`parent_id`),
    UNIQUE KEY `idx_name` (`name`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 17
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='用户标签';


LOCK TABLES `user_tag` WRITE;
/*!40000 ALTER TABLE `user_tag`
    DISABLE KEYS */;
INSERT INTO `user_tag`
VALUES (1, 1, '双一流', 'red', 2, '2024-02-20 09:04:39', '2024-02-20 09:04:39', 0),
       (2, 1, '985', 'purple', 2, '2024-02-20 15:16:23', '2024-02-20 15:16:23', 0),
       (3, 2, '计算机科学', 'red', 2, '2024-02-20 15:29:06', '2024-02-20 15:29:06', 0),
       (4, 4, '后端工程师', 'orange', 2, '2024-02-20 15:30:01', '2024-02-20 15:30:01', 0),
       (5, 5, '求职', 'cyan', 2, '2024-02-20 15:33:43', '2024-02-20 15:33:43', 0),
       (6, 3, '游戏', 'purple', 2, '2024-02-20 15:35:50', '2024-02-20 15:35:50', 0),
       (7, 2, '通信工程', 'green', 2, '2024-02-20 15:38:38', '2024-02-20 15:38:38', 0),
       (8, 1, '研究生', 'orange', 2, '2024-02-20 15:39:56', '2024-02-20 15:39:56', 0),
       (9, 4, '前端开发', 'orange', 2, '2024-02-20 15:41:33', '2024-02-20 15:41:33', 0),
       (10, 5, '工作', 'cyan', 2, '2024-02-20 15:51:35', '2024-02-20 15:51:35', 0),
       (11, 3, '二次元', 'orange', 2, '2024-02-20 15:52:39', '2024-02-20 15:52:39', 0),
       (12, 2, '人工智能', 'pink', 2, '2024-02-20 15:54:14', '2024-02-20 15:54:14', 0),
       (13, 2, '软件工程', 'green', 2, '2024-02-20 15:57:01', '2024-02-20 15:57:01', 0),
       (14, 5, '躺平', 'purple', 2, '2023-04-15 06:11:46', '2023-04-15 06:11:46', 0),
       (15, 1, '一本', 'pink', 3, '2023-05-27 14:45:14', '2023-05-27 14:45:14', 0),
       (16, 5, '学习', 'pink', 3, '2023-05-27 14:45:41', '2023-05-27 14:45:41', 0);
/*!40000 ALTER TABLE `user_tag`
    ENABLE KEYS */;
UNLOCK TABLES;


CREATE TABLE `user_tag_category`
(
    `id`          bigint      NOT NULL AUTO_INCREMENT COMMENT '用户标签大类ID',
    `name`        varchar(64) NOT NULL COMMENT '标签名',
    `create_time` datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_delete`   tinyint(1)  NOT NULL DEFAULT '0' COMMENT '删除标志',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_name` (`name`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 6
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='用户标签大类';


LOCK TABLES `user_tag_category` WRITE;
/*!40000 ALTER TABLE `user_tag_category`
    DISABLE KEYS */;
INSERT INTO `user_tag_category`
VALUES (1, '学历', '2024-02-20 13:46:23', '2024-02-20 13:46:23', 0),
       (2, '专业', '2024-02-20 13:46:40', '2024-02-20 13:46:40', 0),
       (3, '兴趣', '2024-02-20 13:46:49', '2024-02-20 13:46:49', 0),
       (4, '职业', '2024-02-20 13:46:55', '2024-02-20 13:46:55', 0),
       (5, '状态', '2024-02-20 13:47:34', '2024-02-20 13:47:34', 0);
/*!40000 ALTER TABLE `user_tag_category`
    ENABLE KEYS */;
UNLOCK TABLES;

CREATE TABLE `follow`
(
    `id`          bigint   NOT NULL AUTO_INCREMENT COMMENT '关注ID',
    `uid`         bigint   NOT NULL COMMENT '用户ID',
    `follow_uid`  bigint   NOT NULL COMMENT '关注用户ID',
    `unread`      int      NOT NULL DEFAULT '0' COMMENT '未读状态',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_uid` (`uid`),
    UNIQUE KEY `idx_follow_uid` (`follow_uid`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='用户关注';

CREATE TABLE `question`
(
    `id`           bigint   NOT NULL AUTO_INCREMENT COMMENT '题目ID',
    `title`        varchar(512) COLLATE utf8mb4_unicode_ci  DEFAULT NULL COMMENT '标题',
    `content`      text COLLATE utf8mb4_unicode_ci COMMENT '内容',
    `difficulty`   varchar(64) COLLATE utf8mb4_unicode_ci   DEFAULT '简单' COMMENT '难度',
    `tags`         varchar(1024) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '标签列表（json 数组）',
    `answer`       text COLLATE utf8mb4_unicode_ci COMMENT '题目答案',
    `submit_num`   int      NOT NULL                        DEFAULT '0' COMMENT '题目提交数',
    `accepted_num` int      NOT NULL                        DEFAULT '0' COMMENT '题目通过数',
    `judge_case`   text COLLATE utf8mb4_unicode_ci COMMENT '判题用例（json 数组）',
    `judge_config` text COLLATE utf8mb4_unicode_ci COMMENT '判题配置（json 对象）',
    `thumb_num`    int      NOT NULL                        DEFAULT '0' COMMENT '点赞数',
    `favour_num`   int      NOT NULL                        DEFAULT '0' COMMENT '收藏数',
    `user_id`      bigint   NOT NULL COMMENT '创建用户ID',
    `create_time`  datetime NOT NULL                        DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`  datetime NOT NULL                        DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_delete`    tinyint  NOT NULL                        DEFAULT '0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_difficulty` (`difficulty`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='题目';

CREATE TABLE `question_submit`
(
    `id`          bigint                                  NOT NULL AUTO_INCREMENT COMMENT '题目提交ID',
    `language`    varchar(128) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '编程语言',
    `code`        text COLLATE utf8mb4_unicode_ci         NOT NULL COMMENT '用户代码',
    `judge_info`  text COLLATE utf8mb4_unicode_ci COMMENT '判题信息（json 对象）',
    `status`      int                                     NOT NULL DEFAULT '0' COMMENT '判题状态（0 - 待判题、1 - 判题中、2 - 成功、3 - 失败）',
    `question_id` bigint                                  NOT NULL COMMENT '题目ID',
    `user_id`     bigint                                  NOT NULL COMMENT '创建用户ID',
    `create_time` datetime                                NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime                                NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_delete`   tinyint                                 NOT NULL DEFAULT '0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    KEY `idx_question_id` (`question_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_status` (`status`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='题目提交';