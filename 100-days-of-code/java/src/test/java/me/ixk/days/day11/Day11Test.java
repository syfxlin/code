package me.ixk.days.day11;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * @author Otstar Lin
 * @date 2020/12/1 上午 10:48
 */
@Slf4j
class Day11Test {

    private final ThreadPoolExecutor executor = new ThreadPoolExecutor(
        4,
        8,
        0L,
        TimeUnit.MILLISECONDS,
        new SynchronousQueue<>(),
        new AbortPolicy()
    );

    @Test
    void lockProblem() throws InterruptedException {
        final LockProblem p1 = new LockProblem();
        final LockProblem p2 = new LockProblem();
        final CountDownLatch c1 = new CountDownLatch(2);
        this.executor.execute(
                () -> {
                    p1.add1();
                    c1.countDown();
                }
            );
        this.executor.execute(
                () -> {
                    p2.add1();
                    c1.countDown();
                }
            );
        c1.await();
        log.info("Count: {}", LockProblem.count());
        assertNotEquals(2 * LockProblem.LOOP_COUNT, LockProblem.count());

        LockProblem.count(0);
        final CountDownLatch c2 = new CountDownLatch(2);
        this.executor.execute(
                () -> {
                    p1.add2();
                    c2.countDown();
                }
            );
        this.executor.execute(
                () -> {
                    p2.add2();
                    c2.countDown();
                }
            );
        c2.await();
        log.info("Count: {}", LockProblem.count());
        assertNotEquals(2 * LockProblem.LOOP_COUNT, LockProblem.count());

        LockProblem.count(0);
        final CountDownLatch c3 = new CountDownLatch(2);
        this.executor.execute(
                () -> {
                    p1.add3();
                    c3.countDown();
                }
            );
        this.executor.execute(
                () -> {
                    p2.add3();
                    c3.countDown();
                }
            );
        c3.await();
        log.info("Count: {}", LockProblem.count());
        assertNotEquals(2 * LockProblem.LOOP_COUNT, LockProblem.count());
    }

    @Test
    void LockSolve() throws InterruptedException {
        final LockSolve p1 = new LockSolve();
        final LockSolve p2 = new LockSolve();
        final CountDownLatch c1 = new CountDownLatch(2);
        this.executor.execute(
                () -> {
                    p1.add1();
                    c1.countDown();
                }
            );
        this.executor.execute(
                () -> {
                    p2.add1();
                    c1.countDown();
                }
            );
        c1.await();
        log.info("Count: {}", LockSolve.count());
        assertEquals(2 * LockSolve.LOOP_COUNT, LockSolve.count());

        LockSolve.count(0);
        final CountDownLatch c2 = new CountDownLatch(2);
        this.executor.execute(
                () -> {
                    p1.add2();
                    c2.countDown();
                }
            );
        this.executor.execute(
                () -> {
                    p2.add2();
                    c2.countDown();
                }
            );
        c2.await();
        log.info("Count: {}", LockSolve.count());
        assertEquals(2 * LockSolve.LOOP_COUNT, LockSolve.count());
    }

    @Test
    void deadlockProblem() throws InterruptedException {
        DeadlockProblem problem = new DeadlockProblem();
        CountDownLatch count = new CountDownLatch(2);
        final Thread t1 = new Thread(
            () -> {
                problem.lock12();
                count.countDown();
            }
        );
        final Thread t2 = new Thread(
            () -> {
                problem.lock21();
                count.countDown();
            }
        );
        this.executor.execute(t1);
        this.executor.execute(t2);
        LocalDateTime start = LocalDateTime.now();
        count.await(5L, TimeUnit.SECONDS);
        LocalDateTime end = LocalDateTime.now();
        t1.stop();
        t2.stop();
        log.info("Time: {}", end.getSecond() - start.getSecond());
        assertTrue(end.getSecond() - start.getSecond() > 3);
    }

    @Test
    void deadlockSlove() throws InterruptedException {
        DeadlockSlove slove = new DeadlockSlove();
        CountDownLatch count = new CountDownLatch(2);
        final Thread t1 = new Thread(
            () -> {
                slove.lock12();
                count.countDown();
            }
        );
        final Thread t2 = new Thread(
            () -> {
                slove.lock21();
                count.countDown();
            }
        );
        this.executor.execute(t1);
        this.executor.execute(t2);
        LocalDateTime start = LocalDateTime.now();
        count.await(5L, TimeUnit.SECONDS);
        LocalDateTime end = LocalDateTime.now();
        t1.stop();
        t2.stop();
        log.info("Time: {}", end.getSecond() - start.getSecond());
        assertFalse(end.getSecond() - start.getSecond() > 4);
    }
}
