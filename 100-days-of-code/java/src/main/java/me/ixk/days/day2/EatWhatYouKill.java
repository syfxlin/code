/*
 * Copyright (c) 2020, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 *
 */

package me.ixk.days.day2;

import java.util.concurrent.Executor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Otstar Lin
 * @date 2020/10/20 下午 6:07
 */
@Slf4j
public class EatWhatYouKill implements ExecutionStrategy, Runnable {
    private volatile boolean isRunning;

    private enum State {
        /**
         * 空闲
         */
        IDLE,
        /**
         * 生产中
         */
        PRODUCING,
        /**
         * 再次生产中
         */
        REPRODUCING,
    }

    private enum Mode {
        /**
         * 生产-消费分离
         */
        PRODUCE_EXECUTE_CONSUME,
        /**
         * 生产-消费不分离
         */
        EXECUTE_PRODUCE_CONSUME,
    }

    private final AutoLock lock = new AutoLock();
    private final Producer producer;
    private final TryExecutor executor;
    private volatile State state = State.IDLE;

    public EatWhatYouKill(final Producer producer, final Executor executor) {
        this.producer = producer;
        this.executor = TryExecutor.asTryExecutor(executor);
        this.isRunning = true;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    @Override
    public void run() {
        this.execute();
    }

    @Override
    public void execute() {
        try (final AutoLock l = lock.lock()) {
            switch (this.state) {
                case IDLE:
                    this.state = State.PRODUCING;
                    break;
                case PRODUCING:
                    this.state = State.REPRODUCING;
                    return;
                default:
                    return;
            }
        }

        while (isRunning) {
            try {
                if (this.doProduce()) {
                    continue;
                }
                return;
            } catch (final Throwable th) {
                log.warn("Unable to produce", th);
            }
        }
    }

    private boolean doProduce() {
        final Runnable task = this.produceTask();
        if (task == null) {
            try (final AutoLock l = this.lock.lock()) {
                switch (this.state) {
                    case PRODUCING:
                        this.state = State.IDLE;
                        return false;
                    case REPRODUCING:
                        this.state = State.PRODUCING;
                        return true;
                    default:
                        throw new IllegalStateException("State: " + this.state);
                }
            }
        }
        final Mode mode;

        try (final AutoLock l = this.lock.lock()) {
            if (this.executor.tryExecute(this)) {
                this.state = State.IDLE;
                mode = Mode.EXECUTE_PRODUCE_CONSUME;
            } else {
                mode = Mode.PRODUCE_EXECUTE_CONSUME;
            }
        }

        switch (mode) {
            case PRODUCE_EXECUTE_CONSUME:
                this.executeTask(task);
                return true;
            case EXECUTE_PRODUCE_CONSUME:
                this.runTask(task);
                try (final AutoLock l = this.lock.lock()) {
                    if (this.state == State.IDLE) {
                        this.state = State.PRODUCING;
                        return true;
                    }
                }
                return false;
            default:
                throw new IllegalStateException("State: " + this.state);
        }
    }

    private Runnable produceTask() {
        return this.producer.produce();
    }

    private void executeTask(final Runnable task) {
        log.info("Run task in pool");
        this.executor.execute(task);
    }

    private void runTask(final Runnable task) {
        log.info("Run task in loop");
        task.run();
    }
}
