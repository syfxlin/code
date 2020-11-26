/*
 * Copyright (c) 2020, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.days.day7;

import java.util.concurrent.ScheduledExecutorService;

/**
 * 定时任务执行器
 *
 * @author Otstar Lin
 * @date 2020/11/26 上午 9:04
 */
public interface ScheduledTaskExecutor extends ScheduledExecutorService {
    /**
     * 提交 Cron 任务
     *
     * @param cronTask Cron 任务
     */
    void scheduleCron(CronTask cronTask);

    /**
     * 提交 Cron 任务
     *
     * @param runnable 任务
     * @param cron     Cron 语句
     * @param zone     时区
     */
    void scheduleCron(Runnable runnable, String cron, String zone);
}
