package me.ixk.days.day2;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * try-with-resource 自动关闭锁
 *
 * @author Otstar Lin
 * @date 2020/11/18 上午 8:40
 */
public class AutoCloseLock implements AutoCloseable, Serializable {
    private static final long serialVersionUID = 6511951532003324426L;
    private final ReentrantLock lock = new ReentrantLock();

    @Override
    public void close() {
        this.unlock();
    }

    public AutoCloseLock lock() {
        this.lock.lock();
        return this;
    }

    public void lockInterruptibly() throws InterruptedException {
        this.lock.lockInterruptibly();
    }

    public boolean tryLock() {
        return this.lock.tryLock();
    }

    public boolean tryLock(final long time, final TimeUnit unit)
        throws InterruptedException {
        return this.lock.tryLock(time, unit);
    }

    public void unlock() {
        if (this.lock.isLocked()) {
            this.lock.unlock();
        }
    }

    public Condition newCondition() {
        return this.lock.newCondition();
    }
}
