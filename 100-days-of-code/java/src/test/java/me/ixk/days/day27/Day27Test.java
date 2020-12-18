package me.ixk.days.day27;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import java.sql.Connection;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import me.ixk.days.day1.ThreadPoolExecutor;
import org.junit.jupiter.api.Test;

/**
 * @author Otstar Lin
 * @date 2020/12/18 上午 10:31
 */
@Slf4j
class Day27Test {

    @Test
    void swallowException() {
        final Connection c1 = SqlUtil.error();
        // Connection 为 null，同时并没有任何错误信息
        assertNull(c1);

        assertThrows(
            IllegalStateException.class,
            () -> {
                final Connection c2 = SqlUtil.right();
                // 抛出了异常，上层就能根据情况经常处理，出现了 Bug 也能尽快的通过日志找到 Bug
            }
        );
    }

    @Test
    void finallyException() {
        try {
            SqlUtil.finallyError(null);
        } catch (Exception e) {
            // 可以看到 finally 的异常把 catch 里的异常覆盖掉了，对排查造成的困难
            log.error("Finally exception 1", e);
            assertEquals("Finally exception", e.getMessage());
        }

        try {
            SqlUtil.finallyRight2(null);
        } catch (Exception e) {
            // 保留了 catch 的异常，同时把 finally 中的异常包装了起来
            log.error("Finally exception 2", e);
            assertEquals("Operate connection failed", e.getMessage());
        }
    }

    @Test
    void threadException() {
        ThreadPoolExecutor executor = ThreadPoolExecutor.create(
            4,
            8,
            0L,
            TimeUnit.MINUTES,
            20,
            new ThreadFactoryBuilder()
                // 设置线程池未捕获异常处理器，防止提交到线程池中的任务没有做好异常处理，导致异常信息丢失
                // 同时可以防止线程因异常挂掉，导致大量的线程重建
                .setUncaughtExceptionHandler(
                    (thread, th) ->
                        log.error(
                            "ThreadPool " + thread.getName() + " exception",
                            th
                        )
                )
                .build(),
            new AbortPolicy()
        );

        executor.execute(
            () -> {
                throw new RuntimeException("UncaughtException");
            }
        );

        executor.execute(
            () -> {
                // 最好的方式还是应该在任务内执行处理好异常
                try {
                    // 一些操作
                } catch (Exception e) {
                    // 异常处理
                }
            }
        );
    }
}
