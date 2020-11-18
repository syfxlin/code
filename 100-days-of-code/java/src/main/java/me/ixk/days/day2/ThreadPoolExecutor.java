/*
 * Copyright (c) 2020, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 *
 */

package me.ixk.days.day2;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 自定义线程池
 *
 * @author Otstar Lin
 * @date 2020/10/20 上午 8:48
 */
public class ThreadPoolExecutor
    extends java.util.concurrent.ThreadPoolExecutor
    implements TryExecutor {
    private final AtomicInteger submittedTaskCount = new AtomicInteger(0);

    private ThreadPoolExecutor(
        int corePoolSize,
        int maximumPoolSize,
        long keepAliveTime,
        TimeUnit unit,
        TaskQueue<Runnable> workQueue,
        ThreadFactory threadFactory,
        RejectedExecutionHandler handler
    ) {
        super(
            corePoolSize,
            maximumPoolSize,
            keepAliveTime,
            unit,
            workQueue,
            threadFactory,
            handler
        );
    }

    public static ThreadPoolExecutor create(
        int corePoolSize,
        int maximumPoolSize,
        long keepAliveTime,
        TimeUnit unit,
        int queueSize,
        ThreadFactory threadFactory,
        RejectedExecutionHandler handler
    ) {
        TaskQueue<Runnable> taskQueue = new TaskQueue<>(
            queueSize <= 0 ? 1 : queueSize
        );
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
            corePoolSize,
            maximumPoolSize,
            keepAliveTime,
            unit,
            taskQueue,
            threadFactory,
            handler
        );
        taskQueue.setExecutor(executor);
        return executor;
    }

    public int getSubmittedTaskCount() {
        return this.submittedTaskCount.get();
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        // 完成任务后将提交的数量递减一，代表已经完成一个任务
        this.submittedTaskCount.decrementAndGet();
    }

    @Override
    @SuppressWarnings("rawtypes")
    public void execute(Runnable command) {
        if (command == null) {
            throw new NullPointerException();
        }
        // 提交任务的时候递增一，代表有新的任务加入队列
        submittedTaskCount.incrementAndGet();
        try {
            // 实际执行任务
            super.execute(command);
        } catch (RejectedExecutionException rx) {
            // 如果触发拒绝策略，说明有可能是未达到最大线程数，或者工作队列满
            final TaskQueue queue = (TaskQueue) super.getQueue();
            try {
                // 尝试重新插入到工作队列
                if (!queue.retryOffer(command, 0, TimeUnit.MILLISECONDS)) {
                    // 插入失败，说明工作队列实际上满了，触发实际的拒绝策略
                    submittedTaskCount.decrementAndGet();
                    throw new RejectedExecutionException(
                        "Queue capacity is full.",
                        rx
                    );
                }
                // else 插入成功，说明工作队列未满，只是未达到最大线程数，线程创建达到要求的时候就会执行
            } catch (InterruptedException x) {
                submittedTaskCount.decrementAndGet();
                throw new RejectedExecutionException(x);
            }
        } catch (Throwable t) {
            // 出现其他异常，则抛出异常
            submittedTaskCount.decrementAndGet();
            throw t;
        }
    }

    @Override
    public boolean tryExecute(Runnable task) {
        if (this.getSubmittedTaskCount() >= this.getMaximumPoolSize()) {
            return false;
        }
        try {
            this.execute(task);
        } catch (RejectedExecutionException e) {
            return false;
        }
        return true;
    }
}
