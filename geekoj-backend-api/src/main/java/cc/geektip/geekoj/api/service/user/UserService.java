package cc.geektip.geekoj.api.service.user;

import cc.geektip.geekoj.api.model.dto.user.*;
import cc.geektip.geekoj.api.model.entity.user.User;
import cc.geektip.geekoj.api.model.vo.user.RecommendUserVo;
import cc.geektip.geekoj.api.model.vo.user.SocialUser;
import cc.geektip.geekoj.api.model.vo.user.UserInfoVo;
import cc.geektip.geekoj.common.exception.BusinessException;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author Fish
 * @description 针对表【user】的数据库操作Service
 *
 */
public interface UserService extends IService<User> {
    void sendCode(String dest, int type);

    void register(UserRegisterRequest userRegisterRequest);

    void login(AccountLoginRequest accountLoginRequest);

    void logout();

    void oauthLogin(SocialUser socialUser) throws BusinessException;

    UserInfoVo getCurrentUser();

    void updateCurrentUserInfo(UserUpdateRequest request);

    void updatePwd(PwdUpdateRequest pwdUpdateRequest);

    void bindPhone(String phone, String code);

    void updateMail(String mail, String code);

    void loginByPhone(PhoneLoginRequest request);

    UserInfoVo getUserByUid(Long uid);

    List<UserInfoVo> getUserListByUids(List<Long> uids);

    List<RecommendUserVo> getRecommendUsers();

    /**
     * 获取推荐用户，并将推荐用户uid缓存到redis
     * @param uid 当前uid
     * @param tags 当前tags
     * @param tagCount userTag总数
     * @param loopCount 循环次数，超出这个次数还没找到8个相似度超过阈值的，余下的随机填充
     * @return uid和score组成的hashmap
     */
    Map<String, Double> getAndCacheRecommendUserIds(Long uid, String tags, int tagCount, int loopCount);

    /**
     * 获取随机推荐用户，并将推荐用户uid缓存到redis（为未登录用户定制）
     * @param tagCount userTag总数
     * @param loopCount 循环次数，超出这个次数还没找到8个相似度超过阈值的，余下的随机填充
     * @return uid和score组成的hashmap
     */
    Map<String, Double> getAndCacheRandomUserIds(int tagCount, int loopCount);

    List<RecommendUserVo> refreshRecommendUsers();

    List<UsernameAndAvatarDto> listUserNameAndAvatarByUids(Collection<Long> uids);

    UsernameAndAvatarDto getUsernameAndAvatar(Long uid);

    List<RecommendUserVo> getRecommendUserVoList(List<Long> recommendUids, List<Double> scores);

    void incrFollowsCount(Long uid);

    void decrFollowsCount(Long uid);

    void incrFansCount(Long uid);

    void decrFansCount(Long uid);

}

