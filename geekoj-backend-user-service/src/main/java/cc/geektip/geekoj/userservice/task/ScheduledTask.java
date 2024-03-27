package cc.geektip.geekoj.userservice.task;

import cn.dev33.satoken.same.SaSameUtil;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @description: 定时任务类
 * @author: Fish
 * @date: 2024/3/27
 */
public class ScheduledTask {
    /**
     * 定时刷新子服务之间调用令牌
     */
    @Scheduled(cron = "0 0/5 * * * ? ")
    public void refreshToken() {
        SaSameUtil.refreshToken();
    }
}
