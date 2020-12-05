package me.ixk.days.day14;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * @author Otstar Lin
 * @date 2020/12/5 下午 4:19
 */
class Day14Test {

    @Test
    void weakCache() {
        final WeakCache<String, String> cache = new WeakCache<>();
        final String key = "key";
        cache.put(key, "123");
        assertEquals("123", cache.get(key));
    }

    @Test
    void softCache() {
        final SoftCache<String, String> cache = new SoftCache<>();
        final String key = "key";
        cache.put(key, "123");
        assertEquals("123", cache.get(key));
    }
}
