package me.ixk.days.day12;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * @author Otstar Lin
 * @date 2020/12/2 上午 10:54
 */
@Slf4j
class Day12Test {

    private final ThreadPoolExecutor executor = new ThreadPoolExecutor(
        4,
        8,
        0L,
        TimeUnit.MILLISECONDS,
        new SynchronousQueue<>(),
        new AbortPolicy()
    );

    private final LockAllocator allocator = new LockAllocator();

    @Test
    void apply() throws InterruptedException {
        Object o1 = new Object();
        Object o2 = new Object();
        CountDownLatch count = new CountDownLatch(2);
        final Runnable run1 = () -> {
            allocator.apply(o1, o2);
            try {
                synchronized (o1) {
                    Thread.sleep(100L);
                    synchronized (o2) {
                        log.info("Run1");
                        count.countDown();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                allocator.free(o1, o2);
            }
        };
        final Thread t1 = new Thread(run1);
        this.executor.execute(t1);
        final Runnable run2 = () -> {
            allocator.apply(o1, o2);
            try {
                synchronized (o2) {
                    Thread.sleep(100L);
                    synchronized (o1) {
                        log.info("Run2");
                        count.countDown();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                allocator.free(o1, o2);
            }
        };
        final Thread t2 = new Thread(run2);
        this.executor.execute(t2);
        count.await(5L, TimeUnit.SECONDS);
        t1.stop();
        t2.stop();
    }
}
