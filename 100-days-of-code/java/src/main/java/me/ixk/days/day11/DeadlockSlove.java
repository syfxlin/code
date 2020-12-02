package me.ixk.days.day11;

import lombok.extern.slf4j.Slf4j;
import me.ixk.days.day2.AutoLock;

/**
 * 死锁解决
 *
 * @author Otstar Lin
 * @date 2020/12/1 下午 1:18
 */
@Slf4j
public class DeadlockSlove {

    private final AutoLock l1 = new AutoLock();
    private final AutoLock l2 = new AutoLock();

    public void lock12() {
        try (final AutoLock lock1 = l1.lock()) {
            try {
                Thread.sleep(1000);
            } catch (final InterruptedException e) {
                e.printStackTrace();
            }
            try (final AutoLock lock2 = l2.lock()) {
                log.info("lock12");
            }
        }
    }

    public void lock21() {
        // 解决方法很简单，不管如何我们都应该让锁的依赖顺序一致，即都为 l1->l2
        // 这样 lock21 的线程不管怎样，都只能等 l1 的锁释放才进入流程，而 l1 释放前 l2 一定会被释放
        // 这样就不会出现死锁的问题了
        try (final AutoLock lock1 = l1.lock()) {
            try {
                Thread.sleep(1000);
            } catch (final InterruptedException e) {
                e.printStackTrace();
            }
            try (final AutoLock lock2 = l2.lock()) {
                log.info("lock21");
            }
        }
    }
}
