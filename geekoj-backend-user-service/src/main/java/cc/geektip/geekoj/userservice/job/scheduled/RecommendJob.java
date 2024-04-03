package cc.geektip.geekoj.userservice.job.scheduled;

import cc.geektip.geekoj.api.model.entity.user.User;
import cc.geektip.geekoj.api.service.user.UserService;
import cc.geektip.geekoj.api.service.user.UserTagService;
import cc.geektip.geekoj.common.constant.SystemConstant;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

/**
 *  生成推荐用户(5个)并保存至redis，方便用户访问，执行频率是每天1次
 */
// @Component
@Slf4j
public class RecommendJob {
    @Resource
    private UserService userService;
    @Resource
    private UserTagService userTagService;
    @Resource
    private ThreadPoolExecutor threadPoolExecutor;

    /**
     *  用户推荐生成任务
     */
    @Scheduled(cron = "0 0 0 1/1 * ? ")
    public void genRecommendUsers(){
        StopWatch watch = new StopWatch();
        watch.start();

        long tagCount = userTagService.count();
        List<User> userList = userService.lambdaQuery().select(User::getUid, User::getTags).list();
        ArrayList<CompletableFuture<Void>> futures = new ArrayList<>();

        int loopCount = Math.max(1 , userList.size() / SystemConstant.RANDOM_RECOMMEND_BATCH_SIZE);
        for (User user : userList) {
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> userService.getAndCacheRecommendUserIds(user.getUid(), user.getTags(), Math.toIntExact(tagCount), loopCount), threadPoolExecutor);
            futures.add(future);
        }

        // 生成随机用户推荐
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> userService.getAndCacheRandomUserIds(Math.toIntExact(tagCount), loopCount), threadPoolExecutor);
        futures.add(future);

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        watch.stop();
        log.info("生成推荐用户耗时：{}ms", watch.getTotalTimeMillis());
    }
}
