package cc.geektip.geekoj.userservice.job.scheduled;

import cn.dev33.satoken.same.SaSameUtil;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @description: 定时任务类
 * @author: Fish
 *
 */
public class TokenRefreshTask {
    /**
     * 定时刷新子服务之间调用令牌
     */
    @Scheduled(cron = "0 0/5 * * ? ")
    public void refreshToken() {
        SaSameUtil.refreshToken();
    }
}
