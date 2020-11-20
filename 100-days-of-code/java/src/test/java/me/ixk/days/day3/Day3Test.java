/*
 * Copyright (c) 2020, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 *
 */

package me.ixk.days.day3;

import static org.junit.jupiter.api.Assertions.assertEquals;

import cn.hutool.core.util.StrUtil;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

/**
 * @author Otstar Lin
 * @date 2020/11/20 下午 1:25
 */
class Day3Test {

    @Test
    void heap() {
        final ByteBufferPool pool = new ByteBufferPool();
        final ByteBuffer b1 = pool.acquire(1024);
        final ByteBuffer b2 = pool.acquire(2048);
        final ByteBuffer b3 = pool.acquire(4096);
        b1.put("Hello b1".getBytes());
        b2.put("Hello b2".getBytes());
        b3.put("Hello b3".getBytes());
        pool.release(b1);
        pool.release(b2);
        pool.release(b3);
        final ByteBuffer b4 = pool.acquire(1024);
        final ByteBuffer b5 = pool.resetToWrite(b4).put("b4".getBytes()).flip();
        assertEquals("b4", StrUtil.str(b5, StandardCharsets.UTF_8));
    }

    @Test
    void direct() {
        final ByteBufferPool pool = new ByteBufferPool();
        final ByteBuffer b1 = pool.acquireDirect(1024);
        final ByteBuffer b2 = pool.acquireDirect(2048);
        final ByteBuffer b3 = pool.acquireDirect(4096);
        b1.put("Hello b1".getBytes());
        b2.put("Hello b2".getBytes());
        b3.put("Hello b3".getBytes());
        pool.release(b1);
        pool.release(b2);
        pool.release(b3);
        final ByteBuffer b4 = pool.acquireDirect(1024);
        final ByteBuffer b5 = pool.resetToWrite(b4).put("b4".getBytes()).flip();
        assertEquals("b4", StrUtil.str(b5, StandardCharsets.UTF_8));
    }

    @Test
    void min() {
        final ByteBufferPool pool = new ByteBufferPool();
        final ByteBuffer b1 = pool.acquireDirect(512);
        assertEquals(1024, b1.capacity());
    }

    @Test
    void limit() {
        final ByteBufferPool pool = new ByteBufferPool(
            1024,
            1024,
            -1,
            64 * 1024,
            0
        );
        pool.release(pool.acquire(4 * 1024));
        pool.release(pool.acquire(8 * 1024));
        pool.release(pool.acquire(16 * 1024));
        pool.release(pool.acquire(32 * 1024));
        pool.release(pool.acquire(5 * 1024));
        assertEquals(65 * 1024 - 4 * 1024, pool.getMemory(false).intValue());
    }
}
