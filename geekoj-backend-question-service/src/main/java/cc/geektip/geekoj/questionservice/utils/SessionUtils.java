package cc.geektip.geekoj.questionservice.utils;

import cc.geektip.geekoj.api.model.enums.user.UserRoleEnum;
import cc.geektip.geekoj.api.model.vo.user.UserInfoVo;
import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.stereotype.Component;

/**
 * @description: Session工具类
 * @author: Bill Yu
 */
@Component
public class SessionUtils {
    public UserInfoVo getCurrentUser() {
        return StpUtil.getSession().getModel(SaSession.USER, UserInfoVo.class);
    }

    public UserInfoVo getCurrentUserPermitNull() {
        if (!isLogin()) {
            return null;
        }
        return getCurrentUser();
    }

    public Long getCurrentUserId() {
        return StpUtil.getLoginIdAsLong();
    }

    public boolean isLogin() {
        return StpUtil.isLogin();
    }

    public boolean isAdmin() {
        return StpUtil.hasRole(UserRoleEnum.ADMIN.getValue());
    }

    public boolean isBan() {
        return StpUtil.hasRole(UserRoleEnum.BAN.getValue());
    }

    public boolean isCurrentUser(Long userId) {
        return getCurrentUserId().equals(userId);
    }

    public boolean hasUserView(Long userId) {
        if (!isLogin()) {
            return false;
        }
        return isAdmin() || isCurrentUser(userId);
    }

    public boolean hasUserView(UserInfoVo currentUser, Long targetUserId) {
        if (currentUser == null) {
            return false;
        }
        return targetUserId.equals(currentUser.getUid()) || UserRoleEnum.ADMIN.getValue().equals(currentUser.getUserRole());
    }
}
