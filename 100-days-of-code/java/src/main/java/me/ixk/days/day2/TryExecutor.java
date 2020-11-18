/*
 * Copyright (c) 2020, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 *
 */

package me.ixk.days.day2;

import java.util.concurrent.Executor;

/**
 * @author Otstar Lin
 * @date 2020/10/21 下午 6:30
 */
public interface TryExecutor extends Executor {
    /**
     * 尝试执行任务
     *
     * @param task 任务
     *
     * @return 是否成功执行
     */
    boolean tryExecute(Runnable task);

    /**
     * 转换为 TryExecutor
     *
     * @param executor TryExecutor
     *
     * @return TryExecutor
     */
    static TryExecutor asTryExecutor(Executor executor) {
        if (executor instanceof TryExecutor) {
            return (TryExecutor) executor;
        }
        return new NoTryExecutor(executor);
    }

    class NoTryExecutor implements TryExecutor {
        private final Executor executor;

        public NoTryExecutor(Executor executor) {
            this.executor = executor;
        }

        @Override
        public boolean tryExecute(Runnable task) {
            return false;
        }

        @Override
        public void execute(Runnable command) {
            this.executor.execute(command);
        }
    }
}
