package com.liang.bbs.user.service.scheduler;

import com.liang.bbs.user.facade.server.DynamicService;
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
public class UserDynamicWorker {
    @Autowired
    private DynamicService dynamicService;

    @Autowired
    private RedissonClient redissonClient;

    @Async("asyncTaskExecutor")
    @Scheduled(cron = "0 0/1 * * * ?")
    public void threadTask() {
        execute();
    }

    private void execute() {
        RLock lock = redissonClient.getFairLock("user_dynamic_worker" + UUID.randomUUID());

        try {
            boolean b = lock.tryLock();
            if (b) {
                // 鏇存柊鎵€鏈夌敤鎴风殑鍔ㄦ€佷俊鎭?
                dynamicService.updateAll();
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

}
