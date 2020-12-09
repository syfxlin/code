package me.ixk.days.day18;

import static org.junit.jupiter.api.Assertions.assertTrue;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * @author Otstar Lin
 * @date 2020/12/9 下午 7:22
 */
@Slf4j
class Day18Test {

    @Test
    void writeAndRead() {
        // 在写多读少的情况下 CopyOnWrite 的性能比加锁差非常多
        // 在读多写少的情况下 CopyOnWrite 由于不需要加锁，性能会比加锁快不少
        final CopyOnWrite cow = new CopyOnWrite();
        final long sw = System.nanoTime();
        for (int i = 0; i < 100000; i++) {
            cow.addSync(i);
        }
        final long swTime = System.nanoTime() - sw;
        log.info("Sync write time: {}", swTime);
        final long cw = System.nanoTime();
        for (int i = 0; i < 100000; i++) {
            cow.addCow(i);
        }
        final long cwTime = System.nanoTime() - cw;
        log.info("CopyOnWrite write time: {}", cwTime);
        assertTrue(swTime < cwTime);

        final long sr = System.nanoTime();
        for (int i = 0; i < 100000; i++) {
            cow.getSync(i);
        }
        final long srTime = System.nanoTime() - sr;
        log.info("Sync write time: {}", srTime);
        final long cr = System.nanoTime();
        for (int i = 0; i < 100000; i++) {
            cow.getCow(i);
        }
        final long crTime = System.nanoTime() - cr;
        log.info("CopyOnWrite write time: {}", crTime);
        assertTrue(srTime > crTime);
    }
}
