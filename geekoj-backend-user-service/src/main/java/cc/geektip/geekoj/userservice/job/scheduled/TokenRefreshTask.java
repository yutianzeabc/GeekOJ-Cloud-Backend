package cc.geektip.geekoj.userservice.job.scheduled;

import cn.dev33.satoken.same.SaSameUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @description: 定时任务类 - 刷新子服务之间调用令牌
 * @author: Bill Yu
 *
 */
@Component
public class TokenRefreshTask {
    /**
     * 定时刷新子服务之间调用令牌
     */
    @Scheduled(cron = "0 0/5 * * ? ")
    public void refreshToken() {
        SaSameUtil.refreshToken();
    }
}
