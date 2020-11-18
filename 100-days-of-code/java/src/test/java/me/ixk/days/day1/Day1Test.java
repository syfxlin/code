package me.ixk.days.day1;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * @author Otstar Lin
 * @date 2020/11/17 上午 9:56
 */
@Slf4j
class Day1Test {

    @Test
    void threadPool() throws InterruptedException {
        ThreadPoolExecutor executor = ThreadPoolExecutor.create(
            6,
            12,
            0L,
            TimeUnit.MINUTES,
            20,
            Thread::new,
            new AbortPolicy()
        );
        CountDownLatch count = new CountDownLatch(20);
        for (int i = 0; i < 30; i++) {
            executor.execute(
                () -> {
                    log.info(
                        "Pool size {}, Active count  {}, Task count {}",
                        executor.getPoolSize(),
                        executor.getActiveCount(),
                        executor.getTaskCount()
                    );
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        log.error("Thread sleep error", e);
                    } finally {
                        count.countDown();
                    }
                }
            );
        }
        count.await();
    }

    @Test
    void queueFullRejected() throws InterruptedException {
        ThreadPoolExecutor executor = ThreadPoolExecutor.create(
            6,
            12,
            0L,
            TimeUnit.MINUTES,
            20,
            Thread::new,
            new AbortPolicy()
        );
        CountDownLatch count = new CountDownLatch(50);
        RejectedExecutionException exception = null;
        try {
            for (int i = 0; i < 50; i++) {
                executor.execute(
                    () -> {
                        log.info(
                            "Pool size {}, Active count  {}, Task count {}",
                            executor.getPoolSize(),
                            executor.getActiveCount(),
                            executor.getTaskCount()
                        );
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            log.error("Thread sleep error", e);
                        } finally {
                            count.countDown();
                        }
                    }
                );
            }
        } catch (RejectedExecutionException e) {
            exception = e;
        } finally {
            for (long i = 0; i <= count.getCount(); i++) {
                count.countDown();
            }
        }
        assertNotNull(exception);
        count.await();
    }
}
