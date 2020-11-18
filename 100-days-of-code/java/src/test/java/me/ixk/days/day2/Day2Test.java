package me.ixk.days.day2;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;

/**
 * @author Otstar Lin
 * @date 2020/11/18 下午 9:06
 */
class Day2Test {

    @Test
    void produce() throws InterruptedException {
        final ThreadPoolExecutor executor = ThreadPoolExecutor.create(
            2,
            4,
            0L,
            TimeUnit.MINUTES,
            20,
            Thread::new,
            new AbortPolicy()
        );
        CountDownLatch count = new CountDownLatch(10);
        final EatWhatYouKill strategy = new EatWhatYouKill(
            () -> count::countDown,
            executor
        );
        executor.execute(strategy::execute);
        count.await();
        strategy.setRunning(false);
    }
}
