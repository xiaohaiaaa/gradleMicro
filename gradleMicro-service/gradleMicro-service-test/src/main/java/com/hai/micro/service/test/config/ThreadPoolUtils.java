package com.hai.micro.service.test.config;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName ThreadPoolUtil
 * @Description
 * @Author ZXH
 * @Date 2022/7/29 18:25
 * @Version 1.0
 **/
@Slf4j
public class ThreadPoolUtils {

    /**
     * cpu大小
     */
    //private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CPU_COUNT = 2;
    /**
     * 核心线程
     */
    private static final int corePoolSize = Math.max(2, Math.min(CPU_COUNT - 1, 4));
    /**
     * 最大线程
     */
    private static final int maximumPoolSize = CPU_COUNT * 2 + 1;
    /**
     * 线程空闲后的存活时长
     */
    private static final int keepAliveTime = 30;
    /**
     * 线程等待的最长时间
     */
    private static final int defaultAwaitTime = 20;

    /**
     * 阻塞队列
     */
    private static final BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>(2000);

    //线程的创建工厂
    private static final ThreadFactory threadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "micro-thread-" + mCount.getAndIncrement());
        }
    };

    /**
     * 拒绝策略
     */
    private static final RejectedExecutionHandler rejectHandler = new ThreadPoolExecutor.DiscardOldestPolicy();

    /**
     * 构建线程池执行器
     */
    private static final ThreadPoolExecutor pool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime,
            TimeUnit.SECONDS, workQueue, threadFactory, rejectHandler);

    /**
     * 执行任务
     *
     * @param runnable
     */
    public static void submit(Runnable runnable) {
        pool.submit(runnable);
    }

    /**
     * 执行任务--不关心返回结果推荐用这个
     *
     * @param runnable
     */
    public static void execute(Runnable runnable) {
        pool.execute(runnable);
    }

    /**
     * 关闭线程池
     */
    public static boolean shutdown() {
        try {
            pool.shutdown();
            return pool.awaitTermination(defaultAwaitTime,TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.error("关闭线程失败",e);
            return false;
        }
    }

}
