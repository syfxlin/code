/*
 * Copyright (c) 2020, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.days.day7;

import com.cronutils.model.time.ExecutionTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

/**
 * @author Otstar Lin
 * @date 2020/11/26 下午 1:39
 */
public class CronTask {
    private final Runnable task;
    private final ExecutionTime executionTime;
    private final ZoneId zoneId;

    public CronTask(Runnable task, ExecutionTime executionTime, String zone) {
        this.task = task;
        this.executionTime = executionTime;
        this.zoneId =
            zone == null || zone.isEmpty()
                ? ZoneId.systemDefault()
                : ZoneId.of(zone);
    }

    public Runnable getTask() {
        return task;
    }

    public ExecutionTime getExecutionTime() {
        return executionTime;
    }

    public boolean isMatch(ZonedDateTime date) {
        return this.executionTime.isMatch(date);
    }

    public boolean isMatchNow() {
        return this.executionTime.isMatch(ZonedDateTime.now(this.zoneId));
    }

    public Optional<ZonedDateTime> nextExecution(final ZonedDateTime date) {
        return this.executionTime.nextExecution(date);
    }

    public ZoneId getZoneId() {
        return zoneId;
    }
}
