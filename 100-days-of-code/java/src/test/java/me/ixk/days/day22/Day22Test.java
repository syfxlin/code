package me.ixk.days.day22;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;
import java.util.concurrent.TimeUnit;
import me.ixk.days.day1.ThreadPoolExecutor;
import org.junit.jupiter.api.Test;

/**
 * @author Otstar Lin
 * @date 2020/12/13 下午 1:38
 */
class Day22Test {

    @Test
    void threadLocalProblem() throws ExecutionException, InterruptedException {
        ThreadPoolExecutor executor = ThreadPoolExecutor.create(
            1,
            1,
            0L,
            TimeUnit.MINUTES,
            20,
            Thread::new,
            new AbortPolicy()
        );
        ThreadLocalProblem problem = new ThreadLocalProblem();
        final Future<String> f1 = executor.submit(problem::problem);
        assertEquals("Before: init, After: update", f1.get());
        final Future<String> f2 = executor.submit(problem::problem);
        assertNotEquals("Before: init, After: update", f2.get());
        executor.shutdown();
    }

    @Test
    void threadLocalSolve() throws ExecutionException, InterruptedException {
        ThreadPoolExecutor executor = ThreadPoolExecutor.create(
            1,
            1,
            0L,
            TimeUnit.MINUTES,
            20,
            Thread::new,
            new AbortPolicy()
        );
        ThreadLocalProblem problem = new ThreadLocalProblem();
        final Future<String> f1 = executor.submit(problem::solve);
        assertEquals("Before: init, After: update", f1.get());
        final Future<String> f2 = executor.submit(problem::solve);
        assertEquals("Before: init, After: update", f2.get());
        executor.shutdown();
    }

    @Test
    void concurrentUtilProblem() throws InterruptedException {
        ThreadPoolExecutor executor = ThreadPoolExecutor.create(
            4,
            8,
            0L,
            TimeUnit.MINUTES,
            20,
            Thread::new,
            new AbortPolicy()
        );
        ConcurrentUtilProblem problem = new ConcurrentUtilProblem();
        problem.clear();
        CountDownLatch count = new CountDownLatch(3);
        for (int i = 0; i < 3; i++) {
            executor.execute(
                () -> {
                    problem.problemAdd();
                    count.countDown();
                }
            );
        }
        count.await();
        assertNotEquals(
            3 * ConcurrentUtilProblem.SIZE,
            problem.getMap().values().stream().reduce(Integer::sum).get()
        );
        executor.shutdown();
    }

    @Test
    void concurrentUtilSolve() throws InterruptedException {
        ThreadPoolExecutor executor = ThreadPoolExecutor.create(
            4,
            8,
            0L,
            TimeUnit.MINUTES,
            20,
            Thread::new,
            new AbortPolicy()
        );
        ConcurrentUtilProblem problem = new ConcurrentUtilProblem();
        problem.clear();
        CountDownLatch count = new CountDownLatch(3);
        for (int i = 0; i < 3; i++) {
            executor.execute(
                () -> {
                    problem.solveAdd();
                    count.countDown();
                }
            );
        }
        count.await();
        assertEquals(
            3 * ConcurrentUtilProblem.SIZE,
            problem.getMap().values().stream().reduce(Integer::sum).get()
        );
        executor.shutdown();
    }
}
