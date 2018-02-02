package com.suny.association.async.ThreadPool;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.*;

/**************************************
 *  Description  线程池管理类
 *  @author 孙建荣
 *  @date 18-2-2.下午3:30
 *  @version 1.0
 **************************************/
public class CustomerThreadPoolManager {
    /**
     * 获取计算机的cpu核心数量
     */
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();

    /**
     * 核心线程数=CPU核心数+1
     */
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;

    /**
     * 线程池最大线程数=CPU核心数*2+1
     */
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;

    /**
     * 非核心线程闲置超时1s
     */
    private static final int KEEP_ALIVE = 1;
    /**
     * 队列的容量
     */
    private static final int BLOCK_QUEUE_CAPACITY = 20;

    /**
     * 线程池对象
     */
    private ThreadPoolExecutor executor;

    /**
     * 单例保证只能有一个实例对象
     */
    private CustomerThreadPoolManager() {
    }

    private static CustomerThreadPoolManager instance;

    /**
     * 获取单实例对象
     *
     * @return 单实例对象
     */
    public static synchronized CustomerThreadPoolManager getInstance() {
        if (instance == null) {
            instance = new CustomerThreadPoolManager();
        }
        return instance;
    }

    /**
     * 使用线程池
     * corePoolSize:核心线程数
     * maximumPoolSize：线程池所容纳最大线程数(workQueue队列满了之后才开启)
     * keepAliveTime：非核心线程闲置时间超时时长
     * unit：keepAliveTime的单位
     * workQueue：等待队列，存储还未执行的任务
     * threadFactory：线程创建的工厂
     * handler：异常处理机制
     *
     * @param runnable 任务
     */
    public void execute(Runnable runnable) {
        if (executor == null) {
            ThreadFactory build = new ThreadFactoryBuilder().setNameFormat("Thread Pool-%d").build();
            executor = new ThreadPoolExecutor(CustomerThreadPoolManager.CORE_POOL_SIZE,
                    CustomerThreadPoolManager.MAXIMUM_POOL_SIZE,
                    CustomerThreadPoolManager.KEEP_ALIVE,
                    TimeUnit.SECONDS,
                    new ArrayBlockingQueue<>(BLOCK_QUEUE_CAPACITY),
                    build,
                    new ThreadPoolExecutor.AbortPolicy());
        }
        // 把任务丢到线程池里面去
        executor.execute(runnable);
    }

    /**
     * 移除任务从等待队列中移除
     *
     * @param runnable 任务
     */
    public boolean cancel(Runnable runnable) {
        if (runnable != null) {
            return executor.getQueue().remove(runnable);
        }

        return false;
    }


}
