package cc.geektip.geekoj.userservice.config;

import cc.geektip.geekoj.api.service.UserService;
import cc.geektip.geekoj.common.constant.UserConstant;
import cn.dev33.satoken.stp.StpInterface;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import java.util.List;

/**
 * @description: Sa-Token 配置类
 * @author: Fish
 * @date: 2024/3/18
 */
public class SaTokenConfig {
    /**
     * 权限验证接口实现
     */
    @Component
    public static class StpInterfaceImpl implements StpInterface {
        @Resource
        private UserService userService;

        @Override
        public List<String> getPermissionList(Object loginId, String loginType) {
            // 返回此 loginId 拥有的权限列表
            return null;
        }

        @Override
        public List<String> getRoleList(Object loginId, String loginType) {
            // 返回此 loginId 拥有的角色列表
            long userId = (long) loginId;
            if (userService.isAdmin(userId)) {
                return List.of(UserConstant.ADMIN_ROLE);
            } else {
                return List.of(UserConstant.DEFAULT_ROLE);
            }
        }

    }
}
