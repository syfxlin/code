package me.ixk.days.day12;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 锁分配器
 *
 * @author Otstar Lin
 * @date 2020/12/2 上午 10:35
 */
public class LockAllocator {

    private final List<Object> locked = new ArrayList<>();

    public synchronized void apply(final Object... targets) {
        while (true) {
            boolean fail = false;
            for (final Object target : targets) {
                if (this.locked.contains(target)) {
                    try {
                        this.wait();
                        fail = true;
                        break;
                    } catch (final InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            if (!fail) {
                break;
            }
        }
        this.locked.addAll(Arrays.asList(targets));
    }

    public synchronized boolean tryApply(final Object... targets) {
        for (final Object target : targets) {
            if (this.locked.contains(target)) {
                return false;
            }
        }
        this.locked.addAll(Arrays.asList(targets));
        return true;
    }

    public synchronized void free(final Object... targets) {
        this.locked.removeAll(Arrays.asList(targets));
        this.notifyAll();
    }
}
