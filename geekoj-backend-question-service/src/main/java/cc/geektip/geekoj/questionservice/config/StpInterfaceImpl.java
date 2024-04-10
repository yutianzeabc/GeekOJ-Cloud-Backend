package cc.geektip.geekoj.questionservice.config;

import cc.geektip.geekoj.api.model.vo.user.UserInfoVo;
import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @description: Sa-Token 权限验证接口实现类
 * @author: Bill Yu
 *
 */

@Slf4j
@Component
public class StpInterfaceImpl implements StpInterface {

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        // 返回此 loginId 拥有的权限列表
        return null;
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        try {
            UserInfoVo currentUser = StpUtil.getSessionByLoginId(loginId).getModel(SaSession.USER, UserInfoVo.class);
            return List.of(currentUser.getUserRole());
        } catch (Exception e) {
            log.error("获取角色列表失败", e);
            return null;
        }
    }

}
