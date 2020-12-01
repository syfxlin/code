package me.ixk.days.day10;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 原子性问题解决方案
 *
 * @author Otstar Lin
 * @date 2020/11/30 下午 9:07
 */
public class AtomicitySolve {

    public static final int LOOP_COUNT = 100000;
    /**
     * 使用并发包中的原子包装类可以解决原子性问题，当然你也可以加锁解决
     */
    private final AtomicInteger count = new AtomicInteger(0);

    public void add() {
        for (int i = 0; i < LOOP_COUNT; i++) {
            count.incrementAndGet();
        }
    }

    public int count() {
        return count.get();
    }
}
