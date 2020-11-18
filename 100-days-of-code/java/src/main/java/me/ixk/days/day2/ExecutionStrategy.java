/*
 * Copyright (c) 2020, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 *
 */

package me.ixk.days.day2;

/**
 * @author Otstar Lin
 * @date 2020/10/20 下午 5:25
 */
public interface ExecutionStrategy {
    /**
     * 运行
     */
    void execute();

    interface Producer {
        /**
         * 生产任务
         *
         * @return 任务
         */
        Runnable produce();
    }
}
