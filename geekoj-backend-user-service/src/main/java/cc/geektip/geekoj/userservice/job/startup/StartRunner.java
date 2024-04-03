package cc.geektip.geekoj.userservice.job.startup;

import cc.geektip.geekoj.api.model.entity.user.UserTag;
import cc.geektip.geekoj.api.model.entity.user.UserTagCategory;
import cc.geektip.geekoj.api.model.vo.user.UserTagVo;
import cc.geektip.geekoj.api.service.user.UserTagCategoryService;
import cc.geektip.geekoj.api.service.user.UserTagService;
import cc.geektip.geekoj.common.utils.BeanCopyUtils;
import cc.geektip.geekoj.userservice.utils.RedisUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;

import static cc.geektip.geekoj.common.constant.RedisConstant.USER_TAGS_CATEGORY;
import static cc.geektip.geekoj.common.constant.RedisConstant.USER_TAGS_PREFIX;

// @Component
@Slf4j
public class StartRunner implements CommandLineRunner {
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private UserTagService userTagService;
    @Resource
    private UserTagCategoryService userTagCategoryService;
    @Resource
    private RedisUtils redisUtils;

    @Override
    public void run(String... args) {
        //首先查询所有的类别
        List<UserTagCategory> userTagCategories = userTagCategoryService.list();
        if(CollectionUtils.isNotEmpty(userTagCategories)){
            stringRedisTemplate.delete(USER_TAGS_CATEGORY);
            redisUtils.rightPushAllAsString(USER_TAGS_CATEGORY, userTagCategories);
        }

        //根据类别查询标签
        userTagCategories.forEach(userTagCategory -> {
            List<UserTagVo> userTags = userTagService.lambdaQuery()
                    .eq(UserTag::getParentId, userTagCategory.getId())
                    .list()
                    .stream()
                    .map(userTag -> BeanCopyUtils.copyBean(userTag, UserTagVo.class))
                    .toList();
            if(CollectionUtils.isNotEmpty(userTags)){
                String redisKey = USER_TAGS_PREFIX + userTagCategory.getId();
                stringRedisTemplate.delete(redisKey);
                redisUtils.rightPushAllAsString(redisKey, userTags);
            }
        });
    }
}