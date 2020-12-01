package me.ixk.days.day10;

/**
 * 原子性问题
 *
 * @author Otstar Lin
 * @date 2020/11/30 下午 9:07
 */
public class AtomicityProblem {

    public static final int LOOP_COUNT = 100000;
    /**
     * 即使加了 volatile 也无法保证线程安全，因为 count++ 并不是原子操作
     */
    private volatile int count = 0;

    public void add() {
        for (int i = 0; i < LOOP_COUNT; i++) {
            count++;
        }
    }

    public int count() {
        return count;
    }
}
