package me.ixk.days.day10;

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
 * @date 2020/11/30 下午 9:10
 */
@Slf4j
class Day10Test {

    private final ThreadPoolExecutor executor = new ThreadPoolExecutor(
        4,
        8,
        0L,
        TimeUnit.MILLISECONDS,
        new SynchronousQueue<>(),
        new AbortPolicy()
    );

    /**
     * 可见性问题
     * <p>
     * 由于 VisibilityProblem 的 stop 不能实时的被 loop 线程读取到，即不可见 所以导致 loop 线程一直处于 stop
     * = true 的状态，造成死循环
     *
     * @throws InterruptedException 异常
     */
    @Test
    void visibilityProblem() throws InterruptedException {
        VisibilityProblem problem = new VisibilityProblem();
        CountDownLatch count = new CountDownLatch(2);
        this.executor.execute(
                () -> {
                    problem.loop();
                    count.countDown();
                }
            );
        Thread.sleep(100L);
        this.executor.execute(
                () -> {
                    problem.stop();
                    count.countDown();
                }
            );
        LocalDateTime start = LocalDateTime.now();
        count.await(10L, TimeUnit.SECONDS);
        LocalDateTime end = LocalDateTime.now();
        log.info("Time: {}", end.getSecond() - start.getSecond());
        // 判断是否超过指定秒数可以推断出是否发生可见性问题
        assertTrue(end.getSecond() - start.getSecond() > 3);
    }

    @Test
    void visibilitySolve() throws InterruptedException {
        VisibilitySolve solve = new VisibilitySolve();
        CountDownLatch count = new CountDownLatch(2);
        this.executor.execute(
                () -> {
                    solve.loop();
                    count.countDown();
                }
            );
        Thread.sleep(100L);
        this.executor.execute(
                () -> {
                    solve.start();
                    count.countDown();
                }
            );
        LocalDateTime start = LocalDateTime.now();
        count.await(10L, TimeUnit.SECONDS);
        LocalDateTime end = LocalDateTime.now();
        log.info("Time: {}", end.getSecond() - start.getSecond());
        assertFalse(end.getSecond() - start.getSecond() > 3);
    }

    /**
     * 原子性问题
     * <p>
     * 由于 AtomicityProblem 中 count++ 并非原子性操作，总共是三步，1. 读 count、2. count + 1、3. 写
     * count，所以有可能一个线程在 2->3，一个线程在 1->2，那么第一个线程写入的值就会被第二个线程覆盖掉，造成更新丢失，此时的表现就是
     * count 最终的结果不够 20k
     *
     * @throws InterruptedException 异常
     */
    @Test
    void atomicityProblem() throws InterruptedException {
        AtomicityProblem problem = new AtomicityProblem();
        CountDownLatch count = new CountDownLatch(2);
        this.executor.execute(
                () -> {
                    problem.add();
                    count.countDown();
                }
            );
        this.executor.execute(
                () -> {
                    problem.add();
                    count.countDown();
                }
            );
        count.await();
        log.info("Count: {}", problem.count());
        // 不够 20k 时说明发生原子性问题
        assertNotEquals(2 * AtomicityProblem.LOOP_COUNT, problem.count());
    }

    @Test
    void atomicitySolve() throws InterruptedException {
        AtomicitySolve solve = new AtomicitySolve();
        CountDownLatch count = new CountDownLatch(2);
        this.executor.execute(
                () -> {
                    solve.add();
                    count.countDown();
                }
            );
        this.executor.execute(
                () -> {
                    solve.add();
                    count.countDown();
                }
            );
        count.await();
        log.info("Count: {}", solve.count());
        assertEquals(2 * AtomicitySolve.LOOP_COUNT, solve.count());
    }
}
