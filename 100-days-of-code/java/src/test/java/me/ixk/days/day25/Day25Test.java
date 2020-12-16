package me.ixk.days.day25;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * @author Otstar Lin
 * @date 2020/12/16 下午 2:32
 */
@Slf4j
class Day25Test {

    @Test
    void doubleTest() {
        // 由于二进制存储导致 0.1 这类数值无法被准确的存储，所以造成了结果和预想不一致的问题
        // 0.30000000000000004
        log.info("{}", 0.1 + 0.2);
        assertNotEquals(0.3, 0.1 + 0.2);
        // 101.49999999999999
        log.info("{}", 1.015 * 100);
        assertNotEquals(101.5, 1.015 * 100);

        // 0.2 可以被正确存储
        // 0.4
        log.info("{}", 0.2 + 0.2);
        assertEquals(0.4, 0.2 + 0.2);
    }

    @Test
    void bigDecimal() {
        // 虽然 BigDecimal 可以准确的保证浮点数被正确存储和计算，但是如果使用的是 new BigDecimal(double) 的话还是会出现问题
        log.info("{}", new BigDecimal(0.1).add(new BigDecimal(0.2)));
        assertNotEquals(
            "0.3",
            new BigDecimal(0.1).add(new BigDecimal(0.2)).toString()
        );

        // 如果一定要传入 Double，则应该使用 BigDecimal.valueOf 以确保准确性
        log.info("{}", BigDecimal.valueOf(0.1).add(BigDecimal.valueOf(0.2)));
        assertEquals(
            "0.3",
            BigDecimal.valueOf(0.1).add(BigDecimal.valueOf(0.2)).toString()
        );
        // 或者替换为字符串方式传入
        log.info("{}", new BigDecimal("0.1").add(new BigDecimal("0.2")));
        assertEquals(
            "0.3",
            new BigDecimal("0.1").add(new BigDecimal("0.2")).toString()
        );
    }

    @Test
    void round() {
        // 0.3 0.349xxx
        log.info(String.format("%.1f", 0.35F));
        assertEquals("0.3", String.format("%.1f", 0.35F));
        // 0.4 0.350xxx
        log.info(String.format("%.1f", 0.35));
        assertEquals("0.4", String.format("%.1f", 0.35));
    }

    @Test
    void compare() {
        final BigDecimal b1 = new BigDecimal("1.0");
        final BigDecimal b2 = new BigDecimal("1");
        // BigDecimal 在 scale 不同的情况下不能使用 equals 判断是否相等，而应该是用 compareTo
        assertNotEquals(b2, b1);

        assertEquals(0, b1.compareTo(b2));
    }

    @Test
    void map() {
        Set<BigDecimal> s1 = new HashSet<>();
        s1.add(new BigDecimal("1.0"));
        // 由于 scale 不同 1.0 和 1 并不是相等的
        assertFalse(s1.contains(new BigDecimal("1")));

        Set<BigDecimal> s2 = new HashSet<>();
        s2.add(new BigDecimal("1.0").stripTrailingZeros());
        // 去零使 scale 一致
        assertTrue(s2.contains(new BigDecimal("1").stripTrailingZeros()));

        Set<BigDecimal> s3 = new TreeSet<>();
        s3.add(new BigDecimal("1.0"));
        // TreeSet 判断唯一的方式是采用 compareTo，忽略了 scale
        assertTrue(s3.contains(new BigDecimal("1")));
    }
}
