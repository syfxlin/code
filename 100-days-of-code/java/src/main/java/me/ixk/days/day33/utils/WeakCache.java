/*
 * Copyright (c) 2020, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.days.day33.utils;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * 弱引用缓存
 *
 * @author Otstar Lin
 * @date 2020/12/4 下午 10:40
 */
public class WeakCache<K, V> implements ConcurrentMap<K, V>, Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 池
     */
    private final Map<K, V> cache;
    /**
     * 乐观读写锁
     */
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public WeakCache() {
        cache = new WeakHashMap<>();
    }

    public WeakCache(int initialCapacity) {
        cache = new WeakHashMap<>(initialCapacity);
    }

    public WeakCache(Map<K, V> map) {
        cache = map;
    }

    @Override
    public int size() {
        lock.readLock().lock();
        try {
            return cache.size();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        lock.readLock().lock();
        try {
            return cache.containsKey(key);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public boolean containsValue(Object value) {
        lock.readLock().lock();
        try {
            return cache.containsValue(value);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public V get(Object key) {
        lock.readLock().lock();
        try {
            return cache.get(key);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public V put(K key, V value) {
        lock.writeLock().lock();
        try {
            cache.put(key, value);
        } finally {
            lock.writeLock().unlock();
        }
        return value;
    }

    @Override
    public V remove(Object key) {
        lock.writeLock().lock();
        try {
            return cache.remove(key);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        lock.writeLock().lock();
        try {
            cache.putAll(m);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void clear() {
        lock.writeLock().lock();
        try {
            cache.clear();
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public Set<K> keySet() {
        return cache.keySet();
    }

    @Override
    public Collection<V> values() {
        return cache.values();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return cache.entrySet();
    }

    @Override
    public V getOrDefault(Object key, V defaultValue) {
        lock.readLock().lock();
        try {
            return cache.getOrDefault(key, defaultValue);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void forEach(BiConsumer<? super K, ? super V> action) {
        lock.readLock().lock();
        try {
            cache.forEach(action);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public V putIfAbsent(K key, V value) {
        lock.writeLock().lock();
        try {
            return cache.putIfAbsent(key, value);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean remove(Object key, Object value) {
        lock.writeLock().lock();
        try {
            return cache.remove(key, value);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean replace(K key, V oldValue, V newValue) {
        lock.writeLock().lock();
        try {
            return cache.replace(key, oldValue, newValue);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public V replace(K key, V value) {
        lock.writeLock().lock();
        try {
            return cache.replace(key, value);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void replaceAll(
        BiFunction<? super K, ? super V, ? extends V> function
    ) {
        lock.writeLock().lock();
        try {
            cache.replaceAll(function);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public V computeIfAbsent(
        K key,
        Function<? super K, ? extends V> mappingFunction
    ) {
        V v = get(key);
        if (null == v && null != mappingFunction) {
            lock.writeLock().lock();
            try {
                v = cache.get(key);
                // 双重检查，防止在竞争锁的过程中已经有其它线程写入
                if (null == v) {
                    try {
                        v = mappingFunction.apply(key);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    cache.put(key, v);
                }
            } finally {
                lock.writeLock().unlock();
            }
        }
        return v;
    }

    @Override
    public V computeIfPresent(
        K key,
        BiFunction<? super K, ? super V, ? extends V> remappingFunction
    ) {
        V oldValue = get(key);
        if (null != remappingFunction) {
            lock.writeLock().lock();
            try {
                if (oldValue == null) {
                    oldValue = get(key);
                    if (oldValue == null) {
                        return null;
                    }
                }
                V newValue = remappingFunction.apply(key, oldValue);
                if (
                    (newValue == null)
                        ? cache.remove(key, oldValue)
                        : cache.replace(key, oldValue, newValue)
                ) {
                    return newValue;
                }
            } finally {
                lock.writeLock().unlock();
            }
        }
        return null;
    }

    @Override
    public V compute(
        K key,
        BiFunction<? super K, ? super V, ? extends V> remappingFunction
    ) {
        lock.writeLock().lock();
        try {
            V oldValue = get(key);
            V newValue = remappingFunction.apply(key, oldValue);
            if (newValue == null) {
                // delete mapping
                if (oldValue != null || cache.containsKey(key)) {
                    // something to remove
                    cache.remove(key);
                }
                return null;
            } else {
                // add or replace old mapping
                cache.put(key, newValue);
                return newValue;
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public V merge(
        K key,
        V value,
        BiFunction<? super V, ? super V, ? extends V> remappingFunction
    ) {
        lock.writeLock().lock();
        try {
            V oldValue = get(key);
            V newValue = (oldValue == null)
                ? value
                : remappingFunction.apply(oldValue, value);
            if (newValue == null) {
                this.remove(key);
            } else {
                this.put(key, newValue);
            }
            return newValue;
        } finally {
            lock.writeLock().unlock();
        }
    }
}
