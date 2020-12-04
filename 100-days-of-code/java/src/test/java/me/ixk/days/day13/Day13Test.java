package me.ixk.days.day13;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;

/**
 * @author Otstar Lin
 * @date 2020/12/4 上午 10:33
 */
class Day13Test {

    @Test
    void interruptedProblem() throws InterruptedException {
        final CountDownLatch count = new CountDownLatch(1);
        final InterruptedProblem problem = new InterruptedProblem(count);
        problem.start();
        Thread.sleep(2500L);
        // 发送中断请求
        problem.interrupt();
        // 如果中断失败了则会进入下一次循环等待中，导致 await 超时
        final LocalDateTime start = LocalDateTime.now();
        count.await(2L, TimeUnit.SECONDS);
        problem.setRunning(false);
        final LocalDateTime end = LocalDateTime.now();
        assertFalse(end.getSecond() - start.getSecond() < 1);
    }

    @Test
    void interruptedSolve() throws InterruptedException {
        final CountDownLatch count = new CountDownLatch(1);
        final InterruptedSolve solve = new InterruptedSolve(count);
        solve.start();
        Thread.sleep(2500L);
        // 发送中断请求
        solve.interrupt();
        final LocalDateTime start = LocalDateTime.now();
        count.await(2L, TimeUnit.SECONDS);
        solve.setRunning(false);
        final LocalDateTime end = LocalDateTime.now();
        assertTrue(end.getSecond() - start.getSecond() < 1);
    }
}
