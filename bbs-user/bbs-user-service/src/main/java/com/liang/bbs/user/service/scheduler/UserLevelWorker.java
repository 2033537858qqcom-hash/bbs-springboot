package com.liang.bbs.user.service.scheduler;

import com.liang.bbs.user.facade.server.UserLevelService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 */
@Component
@Slf4j
public class UserLevelWorker {
    @Autowired
    private UserLevelService userLevelService;

    @Autowired
    private RedissonClient redissonClient;

    @Async("asyncTaskExecutor")
    @Scheduled(cron = "0 * * * * ?")
    public void threadTask() {
        execute();
    }

    private void execute() {
        RLock lock = redissonClient.getFairLock("user_level_points_worker" + UUID.randomUUID());

        try {
            boolean b = lock.tryLock();
            if (b) {
                // 鏇存柊鎵€鏈夌敤鎴风殑绛夌骇淇℃伅
                userLevelService.updatePointsAll();
            }
        } catch (Exception e) {
            log.error("UserLevelWorker failed!", e);
        } finally {
            // 鐢卞綋鍓嶇嚎绋嬫寔鏈?and 閿佷綇鐘舵€?
            if (lock.isHeldByCurrentThread() && lock.isLocked()) {
                lock.unlock();
            }
        }
    }

    /**
     * 姣?鍒嗛挓鎵ц
     */
    @Async("asyncTaskExecutor")
    @Scheduled(cron = "0 * * * * ?")
    public void threadTask2() {
        executeNull();
    }

    private void executeNull() {
        RLock lock = redissonClient.getFairLock("user_level_null_worker" + UUID.randomUUID());

        try {
            boolean b = lock.tryLock();
            if (b) {
                // 鍚屾鎵€鏈夌敤鎴风殑绛夌骇淇℃伅
                userLevelService.syncAll();
            }
        } catch (Exception e) {
            log.error("UserLevelWorker > executeNull failed!", e);
        } finally {
            // 鐢卞綋鍓嶇嚎绋嬫寔鏈?and 閿佷綇鐘舵€?
            if (lock.isHeldByCurrentThread() && lock.isLocked()) {
                lock.unlock();
            }
        }
    }

}
