package com.liang.bbs.user.service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 */
@EnableAsync
@Configuration
public class TaskThreadPoolConfig {
    @Bean
    public Executor asyncTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 鍒濆鍖栫嚎绋嬫暟
        executor.setCorePoolSize(20);
        // 鏈€澶х嚎绋嬫暟
        executor.setMaxPoolSize(40);
        // 缂撳啿闃熷垪
        executor.setQueueCapacity(100);
        // 鍏佽绌洪棽鏃堕棿
        executor.setKeepAliveSeconds(60);
        // 绾跨▼姹犲悕鍓嶇紑
        executor.setThreadNamePrefix("scheduler-thread-pool-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);
        // 鍒濆鍖?
        executor.initialize();
        return executor;
    }
}
