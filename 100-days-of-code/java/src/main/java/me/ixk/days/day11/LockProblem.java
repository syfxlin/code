package me.ixk.days.day11;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 锁错误使用
 *
 * @author Otstar Lin
 * @date 2020/12/1 上午 10:47
 */
public class LockProblem {

    public static final int LOOP_COUNT = 100000;
    private static int count = 0;
    private final ReentrantLock lock = new ReentrantLock();

    public void add1() {
        for (int i = 0; i < LOOP_COUNT; i++) {
            // 对象级别的锁无法保证类级别的变量的线程安全
            // synchronized 的加锁原理是通过在对象头中写入信息做到的，而不同的对象 this 也是不同的
            // 相当于不同的锁，而不同的锁肯定是没办法保证同一个资源的线程安全性
            synchronized (this) {
                count++;
            }
        }
    }

    public void add2() {
        for (int i = 0; i < LOOP_COUNT; i++) {
            // ReentrantLock 也是类似的，同样是因为不同的锁
            lock.lock();
            try {
                count++;
            } finally {
                lock.unlock();
            }
        }
    }

    public synchronized void add3() {
        // 方法级别的锁其实就是对象锁，相当于 synchronized(this)
        for (int i = 0; i < LOOP_COUNT; i++) {
            count++;
        }
    }

    public static int count() {
        return count;
    }

    public static void count(final int c) {
        count = c;
    }
}
