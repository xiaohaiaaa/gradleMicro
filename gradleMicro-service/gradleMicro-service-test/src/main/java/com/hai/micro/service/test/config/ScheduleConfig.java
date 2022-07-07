package com.hai.micro.service.test.config;

import java.util.concurrent.*;

import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

/**
 * @author 13352
 * @description
 * @date 2021.07.17 10:56
 */
//@Configuration
public class ScheduleConfig implements SchedulingConfigurer {

    ThreadFactory threadFactory =  new ThreadFactoryBuilder().setNameFormat("schedule-pool-%d").build();

    ThreadPoolExecutor threadPool = new ThreadPoolExecutor(1, 10, 3, TimeUnit.MINUTES,
            new SynchronousQueue<>(), threadFactory, new ThreadPoolExecutor.CallerRunsPolicy());

    ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1, threadFactory,
            new ThreadPoolExecutor.CallerRunsPolicy());

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(threadPool);
    }

    //上面new出来的线程池threadPool是ThreadPoolExecutor类型，继承于AbstractExecutorService
    //而实现的SchedulingConfigurer接口需要用到的线程池是ScheduledExecutorService，两者不等，无法使用ThreadPoolExecutor。
    //而如果使用new出来的scheduledExecutorService，会有OOM的风险，因为scheduledExecutorService运行的最大线程数几乎为无限大
    //解决方法：不要使用多线程去开启定时任务，可以在每个定时任务内部再去进行多线程处理，这时候就可以使用ThreadPoolExecutor了。
}
