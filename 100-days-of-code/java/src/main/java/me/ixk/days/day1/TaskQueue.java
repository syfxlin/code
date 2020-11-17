/*
 * Copyright (c) 2020, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 *
 */

package me.ixk.days.day1;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 工作队列
 *
 * @author Otstar Lin
 * @date 2020/10/20 上午 8:56
 */
public class TaskQueue<R extends Runnable>
    extends LinkedBlockingQueue<Runnable> {
    private static final long serialVersionUID = 1L;

    private ThreadPoolExecutor executor;

    public TaskQueue(final int capacity) {
        super(capacity);
    }

    public void setExecutor(final ThreadPoolExecutor exec) {
        executor = exec;
    }

    @Override
    public boolean offer(final Runnable runnable) {
        // 未设置线程池的时候无法获取已提交的数量，抛出异常
        if (executor == null) {
            throw new RejectedExecutionException(
                "The task queue does not have executor!"
            );
        }

        final int currentPoolThreadSize = executor.getPoolSize();
        // 已提交的任务数量少于线程池当前启动的线程数量，则直接添加到工作队列中
        if (executor.getSubmittedTaskCount() < currentPoolThreadSize) {
            return super.offer(runnable);
        }

        // 判断当前线程数量是否达到最大线程数量，如果未达到，则返回 false，让线程池优先新建线程
        if (currentPoolThreadSize < executor.getMaximumPoolSize()) {
            return false;
        }

        // 当当前线程数量达到最大线程数量的时候，此时将任务添加到任务队列中
        return super.offer(runnable);
    }

    public boolean retryOffer(
        final Runnable o,
        final long timeout,
        final TimeUnit unit
    )
        throws InterruptedException {
        if (executor.isShutdown()) {
            throw new RejectedExecutionException("Executor is shutdown!");
        }
        // 重试插入
        return super.offer(o, timeout, unit);
    }
}
