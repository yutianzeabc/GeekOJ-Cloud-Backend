package cc.geektip.geekoj.userservice.utils;

import cc.geektip.geekoj.api.model.vo.user.UserInfoVo;
import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.stereotype.Component;

/**
 * @description: Session工具类
 * @author: Fish
 */
@Component
public class SessionUtil {
    public UserInfoVo getCurrentUser() {
        return StpUtil.getSession().getModel(SaSession.USER, UserInfoVo.class);
    }

    public Long getCurrentUserId() {
        return StpUtil.getLoginIdAsLong();
    }

    public boolean isCurrentUser(Long userId) {
        return getCurrentUserId().equals(userId);
    }
}
