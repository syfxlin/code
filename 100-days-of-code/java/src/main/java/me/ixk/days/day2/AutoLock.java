package me.ixk.days.day2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 自动顺序锁
 *
 * @author Otstar Lin
 * @date 2020/11/17 上午 10:36
 */
public class AutoLock implements AutoCloseable, Serializable {
    private static final long serialVersionUID = 1L;

    private final Map<String, Integer> lockMap;
    private final List<ReentrantLock> lockList;
    private String lastKey;

    public AutoLock(final List<String> lockKeys) {
        this.lockMap = new LinkedHashMap<>(lockKeys.size());
        this.lockList = new ArrayList<>(lockKeys.size());
        for (final String key : lockKeys) {
            this.lockMap.put(key, this.lockList.size());
            this.lockList.add(new ReentrantLock());
        }
        this.lastKey = lockKeys.get(lockKeys.size() - 1);
    }

    public AutoLock(final Map<String, ReentrantLock> lockMap) {
        this.lockMap = new LinkedHashMap<>(lockMap.size());
        this.lockList = new ArrayList<>(lockMap.size());
        for (final Entry<String, ReentrantLock> entry : lockMap.entrySet()) {
            this.lockMap.put(entry.getKey(), this.lockList.size());
            this.lockList.add(entry.getValue());
            if (this.lockMap.size() == lockMap.size()) {
                this.lastKey = entry.getKey();
            }
        }
    }

    @Override
    public void close() {
        this.unlock(this.lastKey);
    }

    public AutoLock lock(final String key) {
        for (int i = 0; i <= this.getLock(key); i++) {
            this.lockList.get(i).lock();
        }
        return this;
    }

    public void lockInterruptibly(final String key)
        throws InterruptedException {
        for (int i = 0; i <= this.getLock(key); i++) {
            this.lockList.get(i).lockInterruptibly();
        }
    }

    public boolean tryLock(final String key) {
        boolean success = true;
        for (int i = 0; i <= this.getLock(key); i++) {
            final boolean result = this.lockList.get(i).tryLock();
            if (!result) {
                success = false;
            }
        }
        return success;
    }

    public boolean tryLock(
        final String key,
        final long time,
        final TimeUnit unit
    )
        throws InterruptedException {
        boolean success = true;
        for (int i = 0; i <= this.getLock(key); i++) {
            final boolean result = this.lockList.get(i).tryLock(time, unit);
            if (!result) {
                success = false;
            }
        }
        return success;
    }

    public void unlock(final String key) {
        for (int i = 0; i <= this.getLock(key); i++) {
            final ReentrantLock lock = this.lockList.get(i);
            if (lock.isLocked()) {
                lock.unlock();
            }
        }
    }

    public Condition newCondition(final String key) {
        return this.lockList.get(this.getLock(key)).newCondition();
    }

    private int getLock(final String key) {
        final Integer index = this.lockMap.get(key);
        if (index == null) {
            throw new NullPointerException(
                "The key is not included in the lock"
            );
        }
        return index;
    }
}
