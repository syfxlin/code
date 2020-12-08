package me.ixk.days.day17;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Otstar Lin
 * @date 2020/12/8 上午 8:22
 */
@Slf4j
public class Tea {

    public static Future<Void> make() {
        final CompletableFuture<Void> c1 = CompletableFuture
            .runAsync(
                () -> {
                    log.info("洗水壶");
                    try {
                        Thread.sleep(1000L);
                    } catch (InterruptedException e) {
                        log.error("Error", e);
                    }
                }
            )
            .thenRun(
                () -> {
                    log.info("烧开水");
                    try {
                        Thread.sleep(15000L);
                    } catch (InterruptedException e) {
                        log.error("Error", e);
                    }
                }
            );
        final CompletableFuture<Void> c2 = CompletableFuture
            .runAsync(
                () -> {
                    log.info("洗茶壶");
                    try {
                        Thread.sleep(1000L);
                    } catch (InterruptedException e) {
                        log.error("Error", e);
                    }
                }
            )
            .thenRun(
                () -> {
                    log.info("洗茶杯");
                    try {
                        Thread.sleep(2000L);
                    } catch (InterruptedException e) {
                        log.error("Error", e);
                    }
                }
            )
            .thenRun(
                () -> {
                    log.info("拿茶叶");
                    try {
                        Thread.sleep(1000L);
                    } catch (InterruptedException e) {
                        log.error("Error", e);
                    }
                }
            );
        return c1
            .thenCombine(c2, (a, b) -> null)
            .thenRun(
                () -> {
                    log.info("泡茶");
                }
            );
    }
}
