package me.ixk.days.day11;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 解决方案
 *
 * @author Otstar Lin
 * @date 2020/12/1 上午 11:12
 */
public class LockSolve {

    public static final int LOOP_COUNT = 100000;
    private static int count = 0;
    private static final ReentrantLock lock = new ReentrantLock();

    public void add1() {
        for (int i = 0; i < LOOP_COUNT; i++) {
            // 更换为类级别的锁即可解决
            synchronized (LockSolve.class) {
                count++;
            }
        }
    }

    public void add2() {
        for (int i = 0; i < LOOP_COUNT; i++) {
            // ReentrantLock 使其变为类级别的变量，即 static，即可解决
            lock.lock();
            try {
                count++;
            } finally {
                lock.unlock();
            }
        }
    }

    public static int count() {
        return count;
    }

    public static void count(final int c) {
        count = c;
    }
}
