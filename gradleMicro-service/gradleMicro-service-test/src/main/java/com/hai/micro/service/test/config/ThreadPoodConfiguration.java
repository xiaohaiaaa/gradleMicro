package com.hai.micro.service.test.config;

import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author 13352
 */
@Configuration
public class ThreadPoodConfiguration {

    @Bean("threadPoolTaskExecutor")
    public ThreadPoolTaskExecutor threadPoodConfiguration() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(2);
        threadPoolTaskExecutor.setMaxPoolSize(3);
        threadPoolTaskExecutor.setKeepAliveSeconds(300);
        threadPoolTaskExecutor.setQueueCapacity(2);
        // 等待队列中的任务全部出队后才关闭线程池，注意是出队完立刻关闭，并不会等出队的任务执行完才关闭，出队了就结束了
        threadPoolTaskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        // 接受到线程池关闭命令之后的最后等待时间，如果线程池中已经没有需要执行或者没有正在执行的任务了，也会自动提前关闭，所以可以设置大一些
        threadPoolTaskExecutor.setAwaitTerminationSeconds(60);
        threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        threadPoolTaskExecutor.setThreadNamePrefix("thread-task-");
        threadPoolTaskExecutor.initialize();
        return threadPoolTaskExecutor;
    }

}
