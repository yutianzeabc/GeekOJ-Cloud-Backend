package cc.geektip.geekoj.userservice.service.impl;

import cc.geektip.geekoj.api.model.entity.user.Follow;
import cc.geektip.geekoj.api.model.entity.user.User;
import cc.geektip.geekoj.api.model.vo.user.FollowVo;
import cc.geektip.geekoj.api.model.vo.user.UserInfoVo;
import cc.geektip.geekoj.api.service.user.FollowService;
import cc.geektip.geekoj.api.service.user.UserService;
import cc.geektip.geekoj.common.constant.RedisConstant;
import cc.geektip.geekoj.userservice.mapper.FollowMapper;
import cc.geektip.geekoj.userservice.mq.FollowMQProducer;
import cc.geektip.geekoj.userservice.utils.SessionUtils;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @description: 关注服务实现
 * @author: Fish
 *
 */

@Slf4j
@DubboService
public class FollowServiceImpl extends ServiceImpl<FollowMapper, Follow> implements FollowService {
    @Resource
    private RedisTemplate<String, Long> longRedisTemplate;
    @Resource
    private FollowMQProducer followMQProducer;
    @Resource
    private SessionUtils sessionUtils;
    @Resource
    private UserService userService;

    @Override
    public void follow(Long uid) {
        // 1. 获取当前用户
        Follow follow = new Follow();
        follow.setUid(sessionUtils.getCurrentUserId());
        follow.setFollowUid(uid);
        // 2. 发送关注消息到队列
        followMQProducer.sendFollowMessage(follow);
    }

    @Override
    public boolean isFollow(Long uid, Long followId) {
        String redisKey = RedisConstant.USER_FOLLOWS_PREFIX + uid;
        // 1. 先判断是否存在
        if (longRedisTemplate.hasKey(redisKey)) {
            // 2. 判断是否关注
            return longRedisTemplate.opsForSet().isMember(redisKey, followId);
        } else {
            // 3. 如果不存在，从数据库中查询
            List<Long> followIds = lambdaQuery().select(Follow::getFollowUid)
                    .eq(Follow::getUid, uid)
                    .list()
                    .stream()
                    .map(Follow::getFollowUid)
                    .toList();
            if (CollUtil.isEmpty(followIds)) {
                return false;
            }
            // 4. 将关注的用户id存入redis
            longRedisTemplate.opsForSet().add(redisKey, followIds.toArray(new Long[0]));
            longRedisTemplate.expire(redisKey, RedisConstant.USER_FOLLOWS_FANS_TTL, TimeUnit.HOURS);
            return followIds.contains(followId);
        }
    }

    @Override
    public List<Long> getFollowIdsOfCurrent() {
        Long currentUid = sessionUtils.getCurrentUserId();
        String redisKey = RedisConstant.USER_FOLLOWS_PREFIX + currentUid;
        // 1. 先判断是否存在
        if (longRedisTemplate.hasKey(redisKey)) {
            // 2. 从redis中获取
            return longRedisTemplate.opsForSet().members(redisKey).stream().toList();
        } else {
            // 3. 从数据库中查询
            List<Long> followIds = lambdaQuery().select(Follow::getFollowUid)
                    .eq(Follow::getUid, currentUid)
                    .list()
                    .stream()
                    .map(Follow::getFollowUid)
                    .toList();
            if (CollUtil.isEmpty(followIds)) {
                return followIds;
            }
            // 4. 将关注的用户id存入redis
            longRedisTemplate.opsForSet().add(redisKey, followIds.toArray(new Long[0]));
            longRedisTemplate.expire(redisKey, RedisConstant.USER_FOLLOWS_FANS_TTL, TimeUnit.HOURS);
            return followIds;
        }
    }

    @Override
    public List<FollowVo> getFollowsOfCurrent() {
        List<Long> followIds = getFollowIdsOfCurrent();
        if (followIds.isEmpty()) {
            return List.of();
        }
        Map<Long, User> followUserMap = userService.listByIds(followIds)
                .stream()
                .collect(Collectors.toMap(User::getUid, user -> user));
        List<FollowVo> vos = lambdaQuery().select(Follow::getFollowUid, Follow::getUnread)
                .in(Follow::getFollowUid, followIds)
                .orderByDesc(Follow::getUpdateTime)
                .list()
                .stream()
                .map(follow -> {
                    FollowVo vo = new FollowVo();
                    vo.setUid(follow.getFollowUid());
                    vo.setUnread(follow.getUnread());
                    vo.setUsername(followUserMap.get(follow.getFollowUid()).getUsername());
                    vo.setAvatar(followUserMap.get(follow.getFollowUid()).getAvatar());
                    return vo;
                }).toList();
        return vos;
    }

    @Override
    public List<Long> getFollowIdsByUid(Long uid) {
        String redisKey = RedisConstant.USER_FOLLOWS_PREFIX + uid;
        // 1. 先判断是否存在
        if (longRedisTemplate.hasKey(redisKey)) {
            // 2. 从redis中获取
            return longRedisTemplate.opsForSet().members(redisKey).stream().toList();
        } else {
            // 3. 从数据库中查询
            List<Long> followIds = lambdaQuery().select(Follow::getFollowUid)
                    .eq(Follow::getUid, uid)
                    .list()
                    .stream()
                    .map(Follow::getFollowUid)
                    .toList();
            if (CollUtil.isEmpty(followIds)) {
                return followIds;
            }
            // 4. 将关注的用户id存入redis
            longRedisTemplate.opsForSet().add(redisKey, followIds.toArray(new Long[0]));
            longRedisTemplate.expire(redisKey, RedisConstant.USER_FOLLOWS_FANS_TTL, TimeUnit.HOURS);
            return followIds;
        }
    }

    @Override
    public List<UserInfoVo> getFollowsByUid(Long uid) {
        List<Long> followIds = lambdaQuery().select(Follow::getFollowUid).eq(Follow::getUid, uid)
                .list().stream().map(Follow::getFollowUid).toList();
        List<UserInfoVo> vos = userService.getUserListByUids(followIds);
        return vos;
    }

    @Override
    public List<Long> getFanIdsOfCurrent() {
        Long currentUid = sessionUtils.getCurrentUserId();
        String redisKey = RedisConstant.USER_FANS_PREFIX + currentUid;
        // 1. 先判断是否存在
        if (longRedisTemplate.hasKey(redisKey)) {
            // 2. 从redis中获取
            return longRedisTemplate.opsForSet().members(redisKey).stream().toList();
        } else {
            // 3. 从数据库中查询
            List<Long> fanIds = lambdaQuery().select(Follow::getUid)
                    .eq(Follow::getFollowUid, currentUid)
                    .list()
                    .stream()
                    .map(Follow::getUid)
                    .toList();
            if (CollUtil.isEmpty(fanIds)) {
                return fanIds;
            }
            // 4. 将粉丝的用户id存入redis
            longRedisTemplate.opsForSet().add(redisKey, fanIds.toArray(new Long[0]));
            longRedisTemplate.expire(redisKey, RedisConstant.USER_FOLLOWS_FANS_TTL, TimeUnit.HOURS);
            return fanIds;
        }
    }

    @Override
    public List<FollowVo> getFansOfCurrent() {
        List<Long> fanIds = getFanIdsOfCurrent();
        if (fanIds.isEmpty()) {
            return List.of();
        }
        Map<Long, User> fanUserMap = userService.listByIds(fanIds)
                .stream()
                .collect(Collectors.toMap(User::getUid, user -> user));
        List<FollowVo> vos = lambdaQuery().select(Follow::getUid, Follow::getUnread)
                .in(Follow::getUid, fanIds)
                .orderByDesc(Follow::getUpdateTime)
                .list()
                .stream()
                .map(follow -> {
                    FollowVo vo = new FollowVo();
                    vo.setUid(follow.getUid());
                    vo.setUnread(follow.getUnread());
                    vo.setUsername(fanUserMap.get(follow.getUid()).getUsername());
                    vo.setAvatar(fanUserMap.get(follow.getUid()).getAvatar());
                    return vo;
                }).toList();
        return vos;
    }

    @Override
    public List<Long> getFanIdsByUid(Long uid) {
        String redisKey = RedisConstant.USER_FANS_PREFIX + uid;
        // 1. 先判断是否存在
        if (longRedisTemplate.hasKey(redisKey)) {
            // 2. 从redis中获取
            return longRedisTemplate.opsForSet().members(redisKey).stream().toList();
        } else {
            // 3. 从数据库中查询
            List<Long> fanIds = lambdaQuery().select(Follow::getUid)
                    .eq(Follow::getFollowUid, uid)
                    .list()
                    .stream()
                    .map(Follow::getUid)
                    .toList();
            if (CollUtil.isEmpty(fanIds)) {
                return fanIds;
            }
            // 4. 将粉丝的用户id存入redis
            longRedisTemplate.opsForSet().add(redisKey, fanIds.toArray(new Long[0]));
            longRedisTemplate.expire(redisKey, RedisConstant.USER_FOLLOWS_FANS_TTL, TimeUnit.HOURS);
            return fanIds;
        }
    }

    @Override
    public List<UserInfoVo> getFansByUid(Long uid) {
        List<Long> fanIds = lambdaQuery().select(Follow::getUid).eq(Follow::getFollowUid, uid)
                .list().stream().map(Follow::getUid).toList();
        List<UserInfoVo> vos = userService.getUserListByUids(fanIds);
        return vos;
    }

    @Override
    public void removeByUidPair(Long uid, Long followUid) {
        lambdaUpdate().eq(Follow::getUid, uid).eq(Follow::getFollowUid, followUid).remove();
    }
}

