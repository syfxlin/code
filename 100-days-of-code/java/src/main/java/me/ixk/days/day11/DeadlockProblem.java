package me.ixk.days.day11;

import lombok.extern.slf4j.Slf4j;
import me.ixk.days.day2.AutoLock;

/**
 * 死锁问题
 *
 * @author Otstar Lin
 * @date 2020/12/1 上午 11:21
 */
@Slf4j
public class DeadlockProblem {

    private final AutoLock l1 = new AutoLock();
    private final AutoLock l2 = new AutoLock();

    public void lock12() {
        try (final AutoLock lock1 = l1.lock()) {
            // 获取锁 l1 后进入等待状态，此时另一个线程会获取锁 l2
            try {
                Thread.sleep(1000);
            } catch (final InterruptedException e) {
                e.printStackTrace();
            }
            // 本线程等待 l2，而另一个线程则等待 l1，但是双方都因为没有走完流程而无法释放锁
            // 此时就出现了循环依赖的问题
            try (final AutoLock lock2 = l2.lock()) {
                log.info("lock12");
            }
        }
    }

    public void lock21() {
        try (final AutoLock lock2 = l2.lock()) {
            try {
                Thread.sleep(1000);
            } catch (final InterruptedException e) {
                e.printStackTrace();
            }
            try (final AutoLock lock1 = l1.lock()) {
                log.info("lock21");
            }
        }
    }
}
