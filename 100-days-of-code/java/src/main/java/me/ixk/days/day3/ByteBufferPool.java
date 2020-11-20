/*
 * Copyright (c) 2020, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 *
 */

package me.ixk.days.day3;

import java.nio.ByteBuffer;
import java.util.Deque;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 对象池
 *
 * @author Otstar Lin
 * @date 2020/11/20 上午 10:25
 */
public class ByteBufferPool {
    private static final int DEFAULT_BUCKET_INCREMENT = 1024;
    private static final int DEFAULT_BUCKET_MAX_SIZE = Integer.MAX_VALUE;
    private static final long MIN_MEMORY = 64 * 1024;

    private final int minCapacity;
    private final int increment;
    private final int bucketMaxSize;
    private final long maxHeapMemory;
    private final long maxDirectMemory;

    private final AtomicLong heapMemory = new AtomicLong(0);
    private final AtomicLong directMemory = new AtomicLong(0);
    private final Map<Integer, Bucket> directBucket = new ConcurrentHashMap<>();
    private final Map<Integer, Bucket> heapBucket = new ConcurrentHashMap<>();

    public ByteBufferPool() {
        this(DEFAULT_BUCKET_INCREMENT);
    }

    public ByteBufferPool(final int increment) {
        this(increment, -1);
    }

    public ByteBufferPool(final int increment, final int minCapacity) {
        this(increment, minCapacity, DEFAULT_BUCKET_MAX_SIZE);
    }

    public ByteBufferPool(
        final int increment,
        final int minCapacity,
        final int bucketMaxSize
    ) {
        this(
            increment,
            minCapacity,
            bucketMaxSize,
            Long.MAX_VALUE,
            Long.MAX_VALUE
        );
    }

    public ByteBufferPool(
        final int increment,
        final int minCapacity,
        final int bucketMaxSize,
        final long maxHeapMemory,
        final long maxDirectMemory
    ) {
        this.increment = increment;
        this.minCapacity = minCapacity;
        this.bucketMaxSize = bucketMaxSize;
        this.maxHeapMemory = Math.max(maxHeapMemory, MIN_MEMORY);
        this.maxDirectMemory = Math.max(maxDirectMemory, MIN_MEMORY);
    }

    public ByteBuffer acquire(final int size) {
        return this.acquire(size, false);
    }

    public ByteBuffer acquireDirect(final int size) {
        return this.acquire(size, true);
    }

    public ByteBuffer acquire(final int size, final boolean direct) {
        final int key = this.getKey(size);
        final int capacity = key * this.increment;
        final Bucket bucket = this.getBucket(key, direct);
        if (bucket == null) {
            return this.newByteBuffer(capacity, direct);
        }
        final ByteBuffer buffer = bucket.acquire();
        if (buffer == null) {
            return this.newByteBuffer(capacity, direct);
        }
        this.decrementMemory(buffer);
        return buffer;
    }

    public void release(final ByteBuffer buffer) {
        if (buffer == null) {
            return;
        }
        final int capacity = buffer.capacity();
        if (capacity % this.increment != 0) {
            return;
        }
        final int key = this.getKey(capacity);
        final Bucket bucket = this.getAndSetBucket(key, buffer.isDirect());
        bucket.release(buffer);
        this.incrementMemory(buffer);
        this.releaseMemory(buffer.isDirect());
    }

    public void clear() {
        this.directBucket.values().forEach(Bucket::clear);
        this.heapBucket.values().forEach(Bucket::clear);
        this.directBucket.clear();
        this.heapBucket.clear();
    }

    public ByteBuffer resetToWrite(final ByteBuffer buffer) {
        buffer.clear();
        return buffer;
    }

    public ByteBuffer resetToRead(final ByteBuffer buffer) {
        return buffer.clear().flip();
    }

    private Bucket newBucket(final int key) {
        return new Bucket(key * this.increment, this.bucketMaxSize);
    }

    private ByteBuffer newByteBuffer(final int capacity, final boolean direct) {
        return direct
            ? ByteBuffer.allocateDirect(capacity)
            : ByteBuffer.allocate(capacity);
    }

    private int getKey(final int size) {
        int key = Math.max(size, this.minCapacity) / this.increment;
        if (size % this.increment != 0) {
            key++;
        }
        return key;
    }

    private Bucket getBucket(final int key, final boolean direct) {
        return (direct ? this.directBucket : this.heapBucket).get(key);
    }

    private Bucket getAndSetBucket(final int key, final boolean direct) {
        return (direct ? this.directBucket : this.heapBucket).computeIfAbsent(
                key,
                this::newBucket
            );
    }

    private void incrementMemory(final ByteBuffer buffer) {
        this.setMemory(buffer, true);
    }

    private void decrementMemory(final ByteBuffer buffer) {
        this.setMemory(buffer, false);
    }

    private void setMemory(final ByteBuffer buffer, final boolean isAdd) {
        final AtomicLong memory = this.getMemory(buffer.isDirect());
        final int capacity = buffer.capacity();
        memory.addAndGet(isAdd ? capacity : -capacity);
    }

    public AtomicLong getMemory(final boolean direct) {
        return direct ? this.directMemory : this.heapMemory;
    }

    private void releaseMemory(final boolean direct) {
        while (true) {
            final long memory = this.getMemory(direct).get();
            if (
                memory <= (direct ? this.maxDirectMemory : this.maxHeapMemory)
            ) {
                return;
            }
            long oldest = Long.MAX_VALUE;
            int index = -1;
            final Map<Integer, Bucket> buckets = direct
                ? this.directBucket
                : this.heapBucket;
            for (Entry<Integer, Bucket> entry : buckets.entrySet()) {
                Bucket bucket = entry.getValue();
                long lastUpdate = bucket.getLastUseTime();
                if (lastUpdate < oldest) {
                    oldest = lastUpdate;
                    index = entry.getKey();
                }
            }
            if (index >= 0) {
                Bucket bucket = buckets.remove(index);
                if (bucket != null) {
                    this.getMemory(direct).addAndGet(-bucket.getMemory());
                    bucket.clear();
                }
            }
        }
    }

    public static class Bucket {
        private final Deque<ByteBuffer> queue = new ConcurrentLinkedDeque<>();
        private final int capacity;
        private final int maxSize;
        private final AtomicInteger size;
        private volatile long lastUseTime = System.nanoTime();

        public Bucket(final int capacity, final int maxSize) {
            this.capacity = capacity;
            this.maxSize = maxSize > 0 ? maxSize : Integer.MAX_VALUE;
            this.size = new AtomicInteger(0);
        }

        public ByteBuffer acquire() {
            final ByteBuffer buffer = this.queue.poll();
            if (buffer == null) {
                return null;
            }
            this.size.decrementAndGet();
            this.lastUseTime = System.nanoTime();
            return buffer;
        }

        public void release(final ByteBuffer buffer) {
            if (this.size.get() >= this.maxSize) {
                return;
            }
            this.size.incrementAndGet();
            this.queue.offer(buffer);
        }

        public void clear() {
            this.size.set(0);
            this.queue.clear();
        }

        public int size() {
            return this.size.get();
        }

        public boolean isFull() {
            return this.size.get() >= this.maxSize;
        }

        public long getLastUseTime() {
            return lastUseTime;
        }

        public int getCapacity() {
            return capacity;
        }

        public int getMemory() {
            return capacity * this.size.get();
        }

        @Override
        public String toString() {
            return (
                "Bucket{" +
                "queue=" +
                queue +
                ", capacity=" +
                capacity +
                ", maxSize=" +
                maxSize +
                ", size=" +
                size +
                '}'
            );
        }
    }
}
