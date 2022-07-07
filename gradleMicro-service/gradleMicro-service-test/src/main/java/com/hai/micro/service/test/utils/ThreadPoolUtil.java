package com.hai.micro.service.test.utils;

import java.util.concurrent.*;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

/**
 * @ClassName ThreadPoolUtil
 * @Description 自定义线程池
 * @Author ZXH
 * @Date 2021/12/3 17:37
 * @Version 1.0
 **/
public class ThreadPoolUtil {

    private static final ThreadFactory THREAD_FACTORY = new ThreadFactoryBuilder().setNameFormat("test-pool-%d").build();

    public static final ExecutorService THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(2, 3, 3,
            TimeUnit.MINUTES, new LinkedBlockingDeque<>(2), THREAD_FACTORY, new ThreadPoolExecutor.AbortPolicy());

    public static void execute(Runnable runnable) {
        THREAD_POOL_EXECUTOR.execute(runnable);
    }

    public static void shutdown() {
        THREAD_POOL_EXECUTOR.shutdown();
    }

    public static Boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return THREAD_POOL_EXECUTOR.awaitTermination(timeout, unit);
    }
}
