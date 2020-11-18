/*
 * Copyright (c) 2020, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 *
 */

package me.ixk.days.day2;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 自动锁
 * <p>
 * 使用 try-resource 语句自动在离开作用域的时候释放锁
 *
 * @author Otstar Lin
 * @date 2020/10/19 下午 6:38
 */
public class AutoLock implements AutoCloseable, Serializable {
    private static final long serialVersionUID = 1L;
    private final ReentrantLock lock = new ReentrantLock();

    @Override
    public void close() {
        this.lock.unlock();
    }

    public AutoLock lock() {
        this.lock.lock();
        return this;
    }

    public void lockInterruptibly() throws InterruptedException {
        this.lock.lockInterruptibly();
    }

    public boolean tryLock() {
        return this.lock.tryLock();
    }

    public boolean tryLock(long time, TimeUnit unit)
        throws InterruptedException {
        return this.lock.tryLock(time, unit);
    }

    public void unlock() {
        this.lock.unlock();
    }

    public Condition newCondition() {
        return this.lock.newCondition();
    }
}
