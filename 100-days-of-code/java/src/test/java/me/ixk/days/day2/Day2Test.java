package me.ixk.days.day2;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * @author Otstar Lin
 * @date 2020/11/17 上午 10:38
 */
@Slf4j
class Day2Test {

    /**
     * 非顺序锁会引发死锁的测试
     *
     * @throws InterruptedException 异常
     */
    @Test
    void deadlock() throws InterruptedException {
        final AutoCloseLock l1 = new AutoCloseLock();
        final AutoCloseLock l2 = new AutoCloseLock();
        final CountDownLatch count = new CountDownLatch(2);
        final Thread t1 = new Thread(
            () -> {
                try (final AutoCloseLock lock1 = l1.lock()) {
                    Thread.sleep(500);
                    try (final AutoCloseLock lock2 = l2.lock()) {
                        Thread.sleep(500);
                        log.info(
                            "Process: {}",
                            Thread.currentThread().getName()
                        );
                        count.countDown();
                    }
                } catch (final InterruptedException e) {
                    log.error("Thread sleep error", e);
                }
            }
        );
        t1.start();
        final Thread t2 = new Thread(
            () -> {
                try (final AutoCloseLock lock2 = l2.lock()) {
                    Thread.sleep(500);
                    try (final AutoCloseLock lock1 = l1.lock()) {
                        Thread.sleep(500);
                        log.info(
                            "Process: {}",
                            Thread.currentThread().getName()
                        );
                        count.countDown();
                    }
                } catch (final InterruptedException e) {
                    log.error("Thread sleep error", e);
                }
            }
        );
        t2.start();
        new Thread(
            () -> {
                try {
                    Thread.sleep(10000);
                } catch (final InterruptedException e) {
                    log.info("Thread sleep error", e);
                } finally {
                    t1.stop();
                    t2.stop();
                    for (long i = 0; i <= count.getCount(); i++) {
                        count.countDown();
                    }
                }
            }
        )
        .start();
        count.await();
    }

    /**
     * 通过自动顺序锁自动处理锁的依赖顺序，防止死锁发生
     *
     * @throws InterruptedException 异常
     */
    @Test
    void solution() throws InterruptedException {
        final AutoLock lock = new AutoLock(List.of("l1", "l2"));
        final CountDownLatch count = new CountDownLatch(2);
        final Thread t1 = new Thread(
            () -> {
                try (final AutoLock lock1 = lock.lock("l1")) {
                    Thread.sleep(500);
                    try (final AutoLock lock2 = lock.lock("l2")) {
                        Thread.sleep(500);
                        log.info(
                            "Process: {}",
                            Thread.currentThread().getName()
                        );
                        count.countDown();
                    }
                } catch (final InterruptedException e) {
                    log.error("Thread sleep error", e);
                }
            }
        );
        t1.start();
        final Thread t2 = new Thread(
            () -> {
                try (final AutoLock lock2 = lock.lock("l2")) {
                    Thread.sleep(500);
                    try (final AutoLock lock1 = lock.lock("l1")) {
                        Thread.sleep(500);
                        log.info(
                            "Process: {}",
                            Thread.currentThread().getName()
                        );
                        count.countDown();
                    }
                } catch (final InterruptedException e) {
                    log.error("Thread sleep error", e);
                }
            }
        );
        t2.start();
        new Thread(
            () -> {
                try {
                    Thread.sleep(10000);
                } catch (final InterruptedException e) {
                    log.info("Thread sleep error", e);
                } finally {
                    t1.stop();
                    t2.stop();
                    for (long i = 0; i <= count.getCount(); i++) {
                        count.countDown();
                    }
                }
            }
        )
        .start();
        count.await();
    }
}
