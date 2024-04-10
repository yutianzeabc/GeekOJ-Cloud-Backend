package cc.geektip.geekoj.api.service.user;

import cc.geektip.geekoj.api.model.dto.user.*;
import cc.geektip.geekoj.api.model.entity.user.User;
import cc.geektip.geekoj.api.model.vo.user.SocialUser;
import cc.geektip.geekoj.api.model.vo.user.UserInfoVo;
import cc.geektip.geekoj.common.exception.BusinessException;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author Bill Yu
 * @description 针对表【user】的数据库操作Service
 *
 */
public interface UserService extends IService<User> {
    void sendCode(String dest, int type);

    void register(UserRegisterRequest userRegisterRequest);

    void login(AccountLoginRequest accountLoginRequest);

    void logout();

    void oauthLogin(SocialUser socialUser) throws BusinessException;

    void updateCurrentUser(UserUpdateRequest request);

    void updatePwd(PwdUpdateRequest pwdUpdateRequest);

    void bindPhone(String phone, String code);

    void updateMail(String mail, String code);

    void loginByPhone(PhoneLoginRequest request);

    UserInfoVo getCurrentUser();

    UserInfoVo getUserByUid(Long uid);

    List<UserInfoVo> getUserListByUids(List<Long> uids);

    void incrFollowsCount(Long uid);

    void decrFollowsCount(Long uid);

    void incrFansCount(Long uid);

    void decrFansCount(Long uid);

}

