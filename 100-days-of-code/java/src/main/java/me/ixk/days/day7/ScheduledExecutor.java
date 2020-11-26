/*
 * Copyright (c) 2020, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.days.day7;

import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.model.time.ExecutionTime;
import com.cronutils.parser.CronParser;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 任务执行器
 *
 * @author Otstar Lin
 * @date 2020/11/26 上午 8:08
 */
public class ScheduledExecutor
    extends ScheduledThreadPoolExecutor
    implements AsyncTaskExecutor, ScheduledTaskExecutor {
    private static final Logger log = LoggerFactory.getLogger(
        ScheduledExecutor.class
    );
    private final List<CronTask> cronTasks = new ArrayList<>();
    private final CronTimer cronTimer = new CronTimer(this, this.cronTasks);
    private final CronParser cronParser = new CronParser(
        CronDefinitionBuilder.instanceDefinitionFor(CronType.SPRING)
    );

    public ScheduledExecutor(final int corePoolSize) {
        super(corePoolSize);
        this.execute(cronTimer);
    }

    public ScheduledExecutor(
        final int corePoolSize,
        final ThreadFactory threadFactory
    ) {
        super(corePoolSize, threadFactory);
        this.execute(cronTimer);
    }

    public ScheduledExecutor(
        final int corePoolSize,
        final RejectedExecutionHandler handler
    ) {
        super(corePoolSize, handler);
        this.execute(cronTimer);
    }

    public ScheduledExecutor(
        final int corePoolSize,
        final ThreadFactory threadFactory,
        final RejectedExecutionHandler handler
    ) {
        super(corePoolSize, threadFactory, handler);
        this.execute(cronTimer);
    }

    @Override
    public void shutdown() {
        log.debug("ScheduledExecutor shutdown");
        this.cronTimer.stop();
        super.shutdown();
    }

    @Override
    public List<Runnable> shutdownNow() {
        log.debug("ScheduledExecutor shutdown");
        this.cronTimer.stop();
        return super.shutdownNow();
    }

    @Override
    public void scheduleCron(final CronTask cronTask) {
        synchronized (this.cronTasks) {
            this.cronTasks.add(cronTask);
        }
    }

    @Override
    public void scheduleCron(
        final Runnable runnable,
        final String cron,
        final String zone
    ) {
        this.scheduleCron(
                new CronTask(
                    runnable,
                    ExecutionTime.forCron(this.cronParser.parse(cron)),
                    zone
                )
            );
    }

    public static final class DaemonThreadFactory implements ThreadFactory {
        private final AtomicInteger atoInteger = new AtomicInteger(0);

        @Override
        public Thread newThread(final Runnable command) {
            final Thread thread = new Thread(command);
            thread.setName("schedule-" + atoInteger.getAndIncrement());
            thread.setDaemon(true);
            return thread;
        }
    }
}
