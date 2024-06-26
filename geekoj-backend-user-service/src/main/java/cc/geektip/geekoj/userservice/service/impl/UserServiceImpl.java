package cc.geektip.geekoj.userservice.service.impl;

import cc.geektip.geekoj.api.model.dto.user.*;
import cc.geektip.geekoj.api.model.entity.user.User;
import cc.geektip.geekoj.api.model.enums.user.UserRoleEnum;
import cc.geektip.geekoj.api.model.vo.user.GiteeUser;
import cc.geektip.geekoj.api.model.vo.user.SocialUser;
import cc.geektip.geekoj.api.model.vo.user.UserInfoVo;
import cc.geektip.geekoj.api.model.vo.user.UserTagVo;
import cc.geektip.geekoj.api.service.user.FollowService;
import cc.geektip.geekoj.api.service.user.UserService;
import cc.geektip.geekoj.api.service.user.UserTagService;
import cc.geektip.geekoj.common.common.AppHttpCodeEnum;
import cc.geektip.geekoj.common.constant.RedisConstant;
import cc.geektip.geekoj.common.exception.BusinessException;
import cc.geektip.geekoj.common.exception.ThrowUtils;
import cc.geektip.geekoj.common.utils.BeanCopyUtils;
import cc.geektip.geekoj.common.utils.HttpUtils;
import cc.geektip.geekoj.userservice.mapper.UserMapper;
import cc.geektip.geekoj.userservice.mq.SendCodeMQProducer;
import cc.geektip.geekoj.userservice.utils.SessionUtils;
import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.BCrypt;
import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static cc.geektip.geekoj.common.constant.SystemConstant.PHONE_CODE;
import static cc.geektip.geekoj.common.constant.SystemConstant.USERNAME_PREFIX;

/**
 * @description: 用户服务实现类
 * @author: Bill Yu
 */

@Slf4j
@DubboService
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Resource
    private UserTagService userTagService;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private RedisTemplate<String, Long> longRedisTemplate;
    @Resource
    private SendCodeMQProducer sendCodeMQProducer;
    @Resource
    private SessionUtils sessionUtils;
    @Resource
    @Lazy
    private FollowService followService;

    @Override
    public void sendCode(String dest, int type) {
        String redisCodeKey = (type == PHONE_CODE ? RedisConstant.CODE_SMS_CACHE_PREFIX : RedisConstant.MAIL_CODE_CACHE_PREFIX) + dest;
        String redisCode = stringRedisTemplate.opsForValue().get(redisCodeKey);
        // 1. 判断用户手机号是否在60s内发送验证码
        if (!StringUtils.isEmpty(redisCode)) {
            long currentTime = Long.parseLong(redisCode.split("_")[1]);
            if (System.currentTimeMillis() - currentTime < 60000) {
                //60s内不能再发
                throw new BusinessException(AppHttpCodeEnum.CODE_EXCEPTION);
            }
        }

        // 2. key的形式是prefix:phone，value是codeNum_系统时间
        int code = RandomUtil.randomInt(100000, 1000000);
        String codeNum = String.valueOf(code);
        String redisCodeValue = codeNum + "_" + System.currentTimeMillis();

        // 存入redis，防止同一个手机号在60s内重复发送验证码
        stringRedisTemplate.opsForValue().set(redisCodeKey, redisCodeValue, 10, TimeUnit.MINUTES);

        // 消息队列发送验证码
        sendCodeMQProducer.sendCodeMessage(type, dest, codeNum);
        log.debug("发送验证码：{}", code);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void register(UserRegisterRequest userRegisterRequest) {
        // 1. 校验验证码
        String code = userRegisterRequest.getCaptcha();

        // 2. 校验验证码
        String redisCodeKey = RedisConstant.MAIL_CODE_CACHE_PREFIX + userRegisterRequest.getEmail();
        String redisCode = stringRedisTemplate.opsForValue().get(redisCodeKey);
        if (StringUtils.isEmpty(redisCode) || !redisCode.split("_")[0].equals(code)) {
            throw new BusinessException(AppHttpCodeEnum.WRONG_CODE);
        }
        stringRedisTemplate.delete(redisCodeKey);

        // 3. 用户注册
        final String userEmail = userRegisterRequest.getEmail();
        synchronized (userEmail.intern()) {
            // 3.1 校验邮箱是否已经注册
            checkEmailUnique(userRegisterRequest.getEmail());

            // 3.2 进行注册
            User user = new User();
            user.setEmail(userRegisterRequest.getEmail());
            user.setPassword(BCrypt.hashpw(userRegisterRequest.getPassword()));
            save(user);

            // 3.3 初始化用户信息
            User userUpdate = new User();
            userUpdate.setUid(user.getUid());
            userUpdate.setUsername(USERNAME_PREFIX + user.getUid());
            userUpdate.setAccessKey(DigestUtil.sha1Hex(user.getUid() + RandomUtil.randomString(32)));
            userUpdate.setSecretKey(DigestUtil.sha1Hex(user.getUid() + RandomUtil.randomString(32)));
            updateById(userUpdate);
        }
    }

    @Override
    public void login(AccountLoginRequest accountLoginRequest) {
        String account = accountLoginRequest.getAccount();
        String password = accountLoginRequest.getPassword();

        // 1. 根据账号查询用户
        User user = lambdaQuery().eq(User::getEmail, account).one();
        // 2. 校验用户是否存在
        if (user == null) {
            throw new BusinessException(AppHttpCodeEnum.ACCOUNT_NOT_EXIST);
        }
        // 3. 校验密码
        if (!BCrypt.checkpw(password, user.getPassword())) {
            throw new BusinessException(AppHttpCodeEnum.WRONG_PASSWORD);
        }
        // 4. 登录成功
        StpUtil.login(user.getUid());
        updateSessionCacheByEntity(user);
    }

    @Override
    public void logout() {
        if (StpUtil.isLogin()) {
            StpUtil.logout();
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void oauthLogin(SocialUser socialUser) throws BusinessException {
        // 1. 发送OAUTH请求
        HashMap<String, String> param = new HashMap<>();
        param.put("access_token", socialUser.getAccess_token());
        HttpResponse giteeResponse;
        String userJson;
        try {
            giteeResponse = HttpUtils.doGet("https://gitee.com", "/api/v5/user", new HashMap<>(), param);
            userJson = EntityUtils.toString(giteeResponse.getEntity());
        } catch (Exception e) {
            throw new BusinessException(AppHttpCodeEnum.THIRD_PARTY_EXCEPTION);
        }
        // 2. 解析用户信息
        GiteeUser giteeUser = JSON.parseObject(userJson, GiteeUser.class);
        String socialId = giteeUser.getId();
        // 3. 查询用户是否登录过
        User user = lambdaQuery().eq(User::getSocialUid, socialId).one();

        if (user != null) {
            // 4. 如果用户存在则直接登录
            // 更新用户的访问令牌的时间和access_token
            User userUpdate = new User();
            userUpdate.setUid(user.getUid());
            userUpdate.setAccessToken(socialUser.getAccess_token());
            userUpdate.setExpiresIn(socialUser.getExpires_in());
            updateById(userUpdate);
            StpUtil.login(user.getUid());
            updateSessionCacheByEntity(user);
        } else {
            // 5. 如果用户不存在则注册
            User userNew = new User();
            userNew.setSocialUid(socialId);
            userNew.setAvatar(giteeUser.getAvatar_url());
            userNew.setUsername(USERNAME_PREFIX + RandomUtil.randomString(6));
            userNew.setAccessToken(socialUser.getAccess_token());
            userNew.setExpiresIn(socialUser.getExpires_in());
            save(userNew);
            // 初始化用户信息
            User userUpdate = new User();
            userUpdate.setUid(userNew.getUid());
            userUpdate.setAccessKey(DigestUtil.sha1Hex(userNew.getUid() + RandomUtil.randomString(32)));
            userUpdate.setSecretKey(DigestUtil.sha1Hex(userNew.getUid() + RandomUtil.randomString(32)));
            updateById(userUpdate);
            // 登录
            StpUtil.login(userNew.getUid());
            updateSessionCacheById(userNew.getUid());
        }
    }

    public void checkPhoneUnique(String phone) throws BusinessException {
        Long count = lambdaQuery().eq(User::getPhone, phone).count();
        if (count > 0) {
            throw new BusinessException(AppHttpCodeEnum.PHONE_EXIST);
        }
    }

    public void checkUsernameUnique(String username) throws BusinessException {
        Long count = lambdaQuery().eq(User::getUsername, username).count();
        if (count > 0) {
            throw new BusinessException(AppHttpCodeEnum.USERNAME_EXIST);
        }
    }

    public void checkEmailUnique(String email) throws BusinessException {
        Long count = lambdaQuery().eq(User::getEmail, email).count();
        if (count > 0) {
            throw new BusinessException(AppHttpCodeEnum.EMAIL_EXIST);
        }
    }

    @Override
    public UserInfoVo getCurrentUser() {
        return sessionUtils.getCurrentUser();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCurrentUser(UserUpdateRequest updateVo) {
        // 当前用户只允许修改自己的信息，管理员可以修改任何用户的信息
        Long currentUid = sessionUtils.getCurrentUserId();
        if (!currentUid.equals(updateVo.getUid()) && !StpUtil.hasRole(UserRoleEnum.ADMIN.getValue())) {
            throw new BusinessException(AppHttpCodeEnum.NO_AUTH);
        }
        // 更新数据库
        User user = BeanCopyUtils.copyBean(updateVo, User.class);
        user.setTags(JSON.toJSONString(updateVo.getTags()));
        updateById(user);
        // 更新缓存
        updateSessionCacheById(updateVo.getUid());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePwd(PwdUpdateRequest pwdUpdateRequest) {
        // 只允许当前用户修改自己的密码
        Long currentUid = sessionUtils.getCurrentUserId();
        // 获取当前用户
        User user = getById(currentUid);
        // 校验原密码
        if (!BCrypt.checkpw(pwdUpdateRequest.getOriginalPwd(), user.getPassword())) {
            throw new BusinessException(AppHttpCodeEnum.WRONG_PASSWORD);
        }
        // 更新数据库
        User userUpdate = new User();
        userUpdate.setUid(currentUid);
        userUpdate.setPassword(BCrypt.hashpw(pwdUpdateRequest.getNewPwd()));
        updateById(userUpdate);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void bindPhone(String phone, String code) {
        // 0. 获取当前用户（同时校验是否登录）
        UserInfoVo userInfoVo = sessionUtils.getCurrentUser();
        // 1. 校验验证码
        String redisSmsCodeKey = RedisConstant.CODE_SMS_CACHE_PREFIX + phone;
        String redisSmsCode = stringRedisTemplate.opsForValue().get(redisSmsCodeKey);
        if (StringUtils.isEmpty(redisSmsCode) || !redisSmsCode.split("_")[0].equals(code)) {
            throw new BusinessException(AppHttpCodeEnum.WRONG_CODE);
        }
        stringRedisTemplate.delete(redisSmsCodeKey);
        // 2. 判断手机号是否已经绑定
        User one = lambdaQuery().eq(User::getPhone, phone).one();
        if (one != null) {
            throw new BusinessException(AppHttpCodeEnum.PHONE_EXIST);
        }
        // 3. 更新数据库
        User user = new User();
        user.setUid(userInfoVo.getUid());
        user.setPhone(phone);
        updateById(user);
        // 4. 更新缓存
        updateSessionCacheById(userInfoVo.getUid());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMail(String mail, String code) {
        // 0. 获取当前用户（同时校验是否登录）
        UserInfoVo userInfoVo = sessionUtils.getCurrentUser();
        // 1. 校验验证码
        String redisMailCodeKey = RedisConstant.MAIL_CODE_CACHE_PREFIX + mail;
        String redisMailCode = stringRedisTemplate.opsForValue().get(redisMailCodeKey);
        if (StringUtils.isEmpty(redisMailCode) || !redisMailCode.split("_")[0].equals(code)) {
            throw new BusinessException(AppHttpCodeEnum.WRONG_CODE);
        }
        stringRedisTemplate.delete(redisMailCodeKey);
        // 2. 判断邮箱是否已经绑定
        User one = lambdaQuery().eq(User::getEmail, mail).one();
        if (one != null) {
            throw new BusinessException(AppHttpCodeEnum.EMAIL_EXIST);
        }
        // 3. 更新数据库
        User user = new User();
        user.setUid(userInfoVo.getUid());
        user.setEmail(mail);
        updateById(user);
        // 4. 更新缓存
        updateSessionCacheById(userInfoVo.getUid());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void loginByPhone(PhoneLoginRequest request) {
        // 1. 校验验证码
        String redisSmsCodeKey = RedisConstant.CODE_SMS_CACHE_PREFIX + request.getPhone();
        String redisSmsCode = stringRedisTemplate.opsForValue().get(redisSmsCodeKey);
        if (StringUtils.isEmpty(redisSmsCode) || !redisSmsCode.split("_")[0].equals(request.getCaptcha())) {
            throw new BusinessException(AppHttpCodeEnum.WRONG_CODE);
        }
        stringRedisTemplate.delete(redisSmsCodeKey);
        // 2. 查询用户
        User user = lambdaQuery().eq(User::getPhone, request.getPhone()).one();
        // 3. 如果用户不存在则自动注册
        if (user == null) {
            User userNew = new User();
            userNew.setPhone(request.getPhone());
            userNew.setUsername(USERNAME_PREFIX + RandomUtil.randomString(6));
            save(userNew);
            // 初始化用户信息
            User userUpdate = new User();
            userUpdate.setUid(userNew.getUid());
            userUpdate.setAccessKey(DigestUtil.sha1Hex(userNew.getUid() + RandomUtil.randomString(32)));
            userUpdate.setSecretKey(DigestUtil.sha1Hex(userNew.getUid() + RandomUtil.randomString(32)));
            updateById(userUpdate);
            // 登录
            StpUtil.login(userNew.getUid());
            updateSessionCacheById(userNew.getUid());
        } else {
            StpUtil.login(user.getUid());
            updateSessionCacheById(user.getUid());
        }
    }

    @Override
    public UserInfoVo getUserByUid(Long uid) {
        User targetUser = getById(uid);
        if (targetUser == null) {
            throw new BusinessException(AppHttpCodeEnum.NOT_EXIST);
        }
        return objToVo(targetUser);
    }

    @Override
    public List<UserInfoVo> getUserListByUids(List<Long> uids) {
        if (uids.isEmpty()) {
            return new ArrayList<>();
        }
        List<User> users = listByIds(uids);
        return users.parallelStream().map(this::objToVo).toList();
    }

    @Override
    public void incrFollowsCount(Long uid) {
        String redisKey = RedisConstant.USER_FOLLOWS_COUNT_PREFIX + uid;
        getBaseMapper().incrFollow(uid);
        if (longRedisTemplate.hasKey(redisKey)) {
            longRedisTemplate.opsForValue().increment(redisKey);
        } else {
            longRedisTemplate.opsForValue().set(redisKey, Long.valueOf(lambdaQuery().eq(User::getUid, uid).one().getFollow()));
        }
        longRedisTemplate.expire(redisKey, RedisConstant.USER_FOLLOWS_FANS_TTL, TimeUnit.HOURS);
    }

    @Override
    public void decrFollowsCount(Long uid) {
        String redisKey = RedisConstant.USER_FOLLOWS_COUNT_PREFIX + uid;
        getBaseMapper().decrFollow(uid);
        if (longRedisTemplate.hasKey(redisKey)) {
            longRedisTemplate.opsForValue().decrement(redisKey);
        } else {
            longRedisTemplate.opsForValue().set(redisKey, Long.valueOf(lambdaQuery().eq(User::getUid, uid).one().getFollow()));
        }
        longRedisTemplate.expire(redisKey, RedisConstant.USER_FOLLOWS_FANS_TTL, TimeUnit.HOURS);
    }

    @Override
    public void incrFansCount(Long uid) {
        String redisKey = RedisConstant.USER_FANS_COUNT_PREFIX + uid;
        getBaseMapper().incrFans(uid);
        if (longRedisTemplate.hasKey(redisKey)) {
            longRedisTemplate.opsForValue().increment(redisKey);
        } else {
            longRedisTemplate.opsForValue().set(redisKey, Long.valueOf(lambdaQuery().eq(User::getUid, uid).one().getFans()));
        }
        longRedisTemplate.expire(redisKey, RedisConstant.USER_FOLLOWS_FANS_TTL, TimeUnit.HOURS);
    }

    @Override
    public void decrFansCount(Long uid) {
        String redisKey = RedisConstant.USER_FANS_COUNT_PREFIX + uid;
        getBaseMapper().decrFans(uid);
        if (longRedisTemplate.hasKey(redisKey)) {
            longRedisTemplate.opsForValue().decrement(redisKey);
        } else {
            longRedisTemplate.opsForValue().set(redisKey, Long.valueOf(lambdaQuery().eq(User::getUid, uid).one().getFans()));
        }
        longRedisTemplate.expire(redisKey, RedisConstant.USER_FOLLOWS_FANS_TTL, TimeUnit.HOURS);
    }

    public UserInfoVo objToVo(User user) {
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
        List<UserTagVo> userTagVos = userTagService.idsToTags(user.getTags());
        userInfoVo.setTags(userTagVos);
        // 判断是否关注
        if (StpUtil.isLogin()) {
            Long currentUid = sessionUtils.getCurrentUserId();
            userInfoVo.setIsFollow(followService.isFollow(currentUid, user.getUid()));
        }
        return userInfoVo;
    }

    public void updateSessionCacheById(Long uid) {
        User user = getById(uid);
        ThrowUtils.throwIf(user == null, AppHttpCodeEnum.NOT_EXIST);
        updateSessionCacheByEntity(user);
    }

    public void updateSessionCacheByEntity(User user) {
        UserInfoVo userInfoVo = objToVo(user);
        StpUtil.getSession().set(SaSession.USER, userInfoVo);
    }
}
