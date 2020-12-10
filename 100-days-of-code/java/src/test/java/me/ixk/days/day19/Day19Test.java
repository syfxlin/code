package me.ixk.days.day19;

import static me.ixk.days.day19.StmTransaction.atomic;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * @author Otstar Lin
 * @date 2020/12/10 上午 10:58
 */
@Slf4j
class Day19Test {

    private final ThreadPoolExecutor executor = new ThreadPoolExecutor(
        4,
        8,
        0L,
        TimeUnit.MILLISECONDS,
        new SynchronousQueue<>(),
        new AbortPolicy()
    );

    @Test
    void atomicTest() throws InterruptedException {
        AtomicTarget problem = new AtomicTarget();
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
        assertEquals(2 * AtomicTarget.LOOP_COUNT, problem.count());
    }

    public static class AtomicTarget {

        public static final int LOOP_COUNT = 100000;

        private final TransactionRef<Integer> count = new TransactionRef<>(0);

        public void add() {
            atomic(
                tr -> {
                    for (int i = 0; i < LOOP_COUNT; i++) {
                        count.set(tr, count.get(tr) + 1);
                    }
                }
            );
        }

        public int count() {
            return count.getRef().getValue();
        }
    }
}
