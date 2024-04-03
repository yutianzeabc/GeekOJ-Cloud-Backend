package cc.geektip.geekoj.userservice.service.impl;

import cc.geektip.geekoj.api.model.dto.user.UserTagAddRequest;
import cc.geektip.geekoj.api.model.entity.user.UserTag;
import cc.geektip.geekoj.api.model.entity.user.UserTagCategory;
import cc.geektip.geekoj.api.model.vo.user.UserTagCategoryVo;
import cc.geektip.geekoj.api.model.vo.user.UserTagVo;
import cc.geektip.geekoj.api.service.user.UserTagService;
import cc.geektip.geekoj.common.common.AppHttpCodeEnum;
import cc.geektip.geekoj.common.exception.BusinessException;
import cc.geektip.geekoj.common.utils.BeanCopyUtils;
import cc.geektip.geekoj.common.utils.ObjectMapperUtils;
import cc.geektip.geekoj.userservice.mapper.UserTagMapper;
import cc.geektip.geekoj.userservice.utils.RedisUtils;
import cc.geektip.geekoj.userservice.utils.SessionUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static cc.geektip.geekoj.common.constant.RedisConstant.USER_TAGS_CATEGORY;
import static cc.geektip.geekoj.common.constant.RedisConstant.USER_TAGS_PREFIX;
import static cc.geektip.geekoj.common.constant.SystemConstant.TAG_COLORS;

/**
 * @description: 用户标签服务实现类
 * @author: Fish
 *
 */
@Slf4j
@DubboService
public class UserTagServiceImpl extends ServiceImpl<UserTagMapper, UserTag> implements UserTagService {
    @Resource
    private SessionUtil sessionUtil;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private RedisUtils redisUtils;

    @Override
    public List<UserTagCategoryVo> getAllTags() {
        //获取大类（常驻Redis）
        List<UserTagCategory> categories = redisUtils.readList(USER_TAGS_CATEGORY, UserTagCategory.class);

        //获取小类
        List<UserTagCategoryVo> userTagCategoryVos = categories.stream().map(userTagCategory -> {
            UserTagCategoryVo userTagCategoryVo = BeanCopyUtils.copyBean(userTagCategory, UserTagCategoryVo.class);
            List<UserTag> tags = redisUtils.readList(USER_TAGS_PREFIX + userTagCategoryVo.getId(), UserTag.class);
            userTagCategoryVo.setTags(tags);
            return userTagCategoryVo;
        }).toList();

        return userTagCategoryVos;
    }

    @Override
    public UserTagVo addATag(UserTagAddRequest userTagAddRequest) {
        // 1. 获取当前用户
        Long currentUid = sessionUtil.getCurrentUserId();

        // 2. 获取标签列表
        List<UserTag> tags = redisUtils.readList(USER_TAGS_PREFIX + userTagAddRequest.getParentId(), UserTag.class);

        // 3. 判断标签是否存在
        for (UserTag tag : tags) {
            if(tag.getName().equals(userTagAddRequest.getName())){
                throw new BusinessException(AppHttpCodeEnum.USER_TAG_EXIST);
            }
        }

        // 4. 插入到mysql和redis中（随机生成一个颜色）
        UserTag userTag = BeanCopyUtils.copyBean(userTagAddRequest, UserTag.class);
        userTag.setCreatedBy(currentUid);
        userTag.setColor(TAG_COLORS[new Random().nextInt(TAG_COLORS.length)]);
        save(userTag);

        UserTagVo userTagVo = BeanCopyUtils.copyBean(userTag, UserTagVo.class);
        stringRedisTemplate.opsForList().rightPush(USER_TAGS_PREFIX + userTag.getParentId(), ObjectMapperUtils.writeValueAsString(userTagVo));
        return userTagVo;
    }

    @Override
    public List<UserTagVo> idsToTags(String idsJSON){
        List<Long> tagIds = JSON.parseObject(idsJSON, new TypeReference<>(){});
        return idsToTags(tagIds);
    }

    @Override
    public List<UserTagVo> idsToTags(List<Long> tagIds){
        if(tagIds.isEmpty()){
            return new ArrayList<>();
        }
        return listByIds(tagIds).stream().map(userTag -> BeanCopyUtils.copyBean(userTag, UserTagVo.class)).collect(Collectors.toList());
    }
}

