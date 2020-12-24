/*
 * Copyright (c) 2020, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.days.day33.utils;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;

/**
 * @author Otstar Lin
 * @date 2020/12/2 下午 12:05
 */
public class SoftHashMap<K, V> extends AbstractMap<K, V> implements Map<K, V> {

    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    private static final int MAXIMUM_CAPACITY = 1 << 30;

    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private static final Object NULL_KEY = new Object();
    private final float loadFactor;
    private final ReferenceQueue<K> queue = new ReferenceQueue<K>();
    transient volatile Set<K> keySet = null;
    transient volatile Collection<V> values = null;
    private Entry<K, V>[] table;
    private int size;
    private int threshold;
    private volatile int modCount;
    private transient Set<Map.Entry<K, V>> entrySet = null;

    @SuppressWarnings("unchecked")
    public SoftHashMap(int initialCapacity, final float loadFactor) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException(
                "Illegal Initial Capacity: " + initialCapacity
            );
        }
        if (initialCapacity > MAXIMUM_CAPACITY) {
            initialCapacity = MAXIMUM_CAPACITY;
        }

        if (loadFactor <= 0 || Float.isNaN(loadFactor)) {
            throw new IllegalArgumentException(
                "Illegal Load factor: " + loadFactor
            );
        }
        int capacity = 1;
        while (capacity < initialCapacity) {
            capacity <<= 1;
        }
        table = new Entry[capacity];
        this.loadFactor = loadFactor;
        threshold = (int) (capacity * loadFactor);
    }

    public SoftHashMap(final int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    @SuppressWarnings("unchecked")
    public SoftHashMap() {
        this.loadFactor = DEFAULT_LOAD_FACTOR;
        threshold = DEFAULT_INITIAL_CAPACITY;
        table = new Entry[DEFAULT_INITIAL_CAPACITY];
    }

    public SoftHashMap(final Map<? extends K, ? extends V> m) {
        this(
            Math.max((int) (m.size() / DEFAULT_LOAD_FACTOR) + 1, 16),
            DEFAULT_LOAD_FACTOR
        );
        putAll(m);
    }

    private static Object maskNull(final Object key) {
        return (key == null ? NULL_KEY : key);
    }

    private static <K> K unmaskNull(final K key) {
        return (key == NULL_KEY ? null : key);
    }

    static boolean eq(final Object x, final Object y) {
        return x == y || x.equals(y);
    }

    static int indexFor(final int h, final int length) {
        return h & (length - 1);
    }

    static int hash(int h) {
        h ^= (h >>> 20) ^ (h >>> 12);
        return h ^ (h >>> 7) ^ (h >>> 4);
    }

    private void expungeStaleEntries() {
        Entry<?, ?> e;
        while ((e = (Entry<?, ?>) queue.poll()) != null) {
            final int h = e.hash;
            final int i = indexFor(h, table.length);

            Entry<K, V> prev = table[i];
            Entry<K, V> p = prev;
            while (p != null) {
                final Entry<K, V> next = p.next;
                if (p == e) {
                    if (prev == e) {
                        table[i] = next;
                    } else {
                        prev.next = next;
                    }
                    e.next = null;
                    e.value = null;
                    size--;
                    break;
                }
                prev = p;
                p = next;
            }
        }
    }

    private Entry<K, V>[] getTable() {
        expungeStaleEntries();
        return table;
    }

    @Override
    public int size() {
        if (size == 0) {
            return 0;
        }
        expungeStaleEntries();
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean containsValue(final Object value) {
        if (value == null) {
            return containsNullValue();
        }

        final Entry<K, V>[] tab = getTable();
        for (int i = tab.length; i-- > 0;) {
            for (Entry<K, V> e = tab[i]; e != null; e = e.next) {
                if (value.equals(e.value)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean containsKey(final Object key) {
        return getEntry(key) != null;
    }

    @Override
    public V get(final Object key) {
        final Object k = maskNull(key);
        final int h = hash(k.hashCode());
        final Entry<K, V>[] tab = getTable();
        final int index = indexFor(h, tab.length);
        Entry<K, V> e = tab[index];
        while (e != null) {
            if (e.hash == h && eq(k, e.get())) {
                return e.value;
            }
            e = e.next;
        }
        return null;
    }

    @Override
    public V put(final K key, final V value) {
        @SuppressWarnings("unchecked")
        final K k = (K) maskNull(key);
        final int h = hash(k.hashCode());
        final Entry<K, V>[] tab = getTable();
        final int i = indexFor(h, tab.length);

        for (Entry<K, V> e = tab[i]; e != null; e = e.next) {
            if (h == e.hash && eq(k, e.get())) {
                final V oldValue = e.value;
                if (value != oldValue) {
                    e.value = value;
                }
                return oldValue;
            }
        }

        modCount++;
        final Entry<K, V> e = tab[i];
        tab[i] = new Entry<K, V>(k, value, queue, h, e);
        if (++size >= threshold) {
            resize(tab.length * 2);
        }
        return null;
    }

    @Override
    public V remove(final Object key) {
        final Object k = maskNull(key);
        final int h = hash(k.hashCode());
        final Entry<K, V>[] tab = getTable();
        final int i = indexFor(h, tab.length);
        Entry<K, V> prev = tab[i];
        Entry<K, V> e = prev;

        while (e != null) {
            final Entry<K, V> next = e.next;
            if (h == e.hash && eq(k, e.get())) {
                modCount++;
                size--;
                if (prev == e) {
                    tab[i] = next;
                } else {
                    prev.next = next;
                }
                return e.value;
            }
            prev = e;
            e = next;
        }

        return null;
    }

    @Override
    public void putAll(final Map<? extends K, ? extends V> m) {
        final int numKeysToBeAdded = m.size();
        if (numKeysToBeAdded == 0) {
            return;
        }

        if (numKeysToBeAdded > threshold) {
            int targetCapacity = (int) (numKeysToBeAdded / loadFactor + 1);
            if (targetCapacity > MAXIMUM_CAPACITY) {
                targetCapacity = MAXIMUM_CAPACITY;
            }
            int newCapacity = table.length;
            while (newCapacity < targetCapacity) {
                newCapacity <<= 1;
            }
            if (newCapacity > table.length) {
                resize(newCapacity);
            }
        }

        for (final Map.Entry<? extends K, ? extends V> e : m.entrySet()) {
            put(e.getKey(), e.getValue());
        }
    }

    @Override
    public void clear() {
        while (queue.poll() != null) {}

        modCount++;
        final Entry<K, V>[] tab = table;
        for (int i = 0; i < tab.length; ++i) {
            tab[i] = null;
        }
        size = 0;

        while (queue.poll() != null) {}
    }

    @Override
    public Set<K> keySet() {
        final Set<K> ks = keySet;
        return (ks != null ? ks : (keySet = new KeySet()));
    }

    @Override
    public Collection<V> values() {
        final Collection<V> vs = values;
        return (vs != null ? vs : (values = new Values()));
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        final Set<Map.Entry<K, V>> es = entrySet;
        return es != null ? es : (entrySet = new EntrySet());
    }

    Entry<K, V> getEntry(final Object key) {
        final Object k = maskNull(key);
        final int h = hash(k.hashCode());
        final Entry<K, V>[] tab = getTable();
        final int index = indexFor(h, tab.length);
        Entry<K, V> e = tab[index];
        while (e != null && !(e.hash == h && eq(k, e.get()))) {
            e = e.next;
        }
        return e;
    }

    void resize(final int newCapacity) {
        final Entry<K, V>[] oldTable = getTable();
        final int oldCapacity = oldTable.length;
        if (oldCapacity == MAXIMUM_CAPACITY) {
            threshold = Integer.MAX_VALUE;
            return;
        }
        @SuppressWarnings("unchecked")
        final Entry<K, V>[] newTable = new Entry[newCapacity];
        transfer(oldTable, newTable);
        table = newTable;

        if (size >= threshold / 2) {
            threshold = (int) (newCapacity * loadFactor);
        } else {
            expungeStaleEntries();
            transfer(newTable, oldTable);
            table = oldTable;
        }
    }

    private void transfer(final Entry<K, V>[] src, final Entry<K, V>[] dest) {
        for (int j = 0; j < src.length; ++j) {
            Entry<K, V> e = src[j];
            src[j] = null;
            while (e != null) {
                final Entry<K, V> next = e.next;
                final Object key = e.get();
                if (key == null) {
                    e.next = null;
                    e.value = null;
                    size--;
                } else {
                    final int i = indexFor(e.hash, dest.length);
                    e.next = dest[i];
                    dest[i] = e;
                }
                e = next;
            }
        }
    }

    Entry<K, V> removeMapping(final Object o) {
        if (!Map.Entry.class.isInstance(o)) {
            return null;
        }
        final Entry<K, V>[] tab = getTable();
        final Map.Entry<?, ?> entry = (Map.Entry<?, ?>) o;
        final Object k = maskNull(entry.getKey());
        final int h = hash(k.hashCode());
        final int i = indexFor(h, tab.length);
        Entry<K, V> prev = tab[i];
        Entry<K, V> e = prev;

        while (e != null) {
            final Entry<K, V> next = e.next;
            if (h == e.hash && e.equals(entry)) {
                modCount++;
                size--;
                if (prev == e) {
                    tab[i] = next;
                } else {
                    prev.next = next;
                }
                return e;
            }
            prev = e;
            e = next;
        }

        return null;
    }

    private boolean containsNullValue() {
        final Entry<K, V>[] tab = getTable();
        for (int i = tab.length; i-- > 0;) {
            for (Entry<K, V> e = tab[i]; e != null; e = e.next) {
                if (e.value == null) {
                    return true;
                }
            }
        }
        return false;
    }

    private static class Entry<K, V>
        extends SoftReference<K>
        implements Map.Entry<K, V> {

        private final int hash;
        private V value;
        private Entry<K, V> next;

        /**
         * Creates new entry.
         */
        Entry(
            final K key,
            final V value,
            final ReferenceQueue<K> queue,
            final int hash,
            final Entry<K, V> next
        ) {
            super(key, queue);
            this.value = value;
            this.hash = hash;
            this.next = next;
        }

        @Override
        public K getKey() {
            return SoftHashMap.<K>unmaskNull(get());
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(final V newValue) {
            final V oldValue = value;
            value = newValue;
            return oldValue;
        }

        @Override
        public int hashCode() {
            final Object k = getKey();
            final Object v = getValue();
            return (
                (k == null ? 0 : k.hashCode()) ^ (v == null ? 0 : v.hashCode())
            );
        }

        @Override
        public boolean equals(final Object o) {
            if (!Map.Entry.class.isInstance(o)) {
                return false;
            }
            final Map.Entry<?, ?> e = (Map.Entry<?, ?>) o;
            final Object k1 = getKey();
            final Object k2 = e.getKey();
            if (Objects.equals(k1, k2)) {
                final Object v1 = getValue();
                final Object v2 = e.getValue();
                return Objects.equals(v1, v2);
            }
            return false;
        }

        @Override
        public String toString() {
            return getKey() + "=" + getValue();
        }
    }

    // Views

    private abstract class HashIterator<T> implements Iterator<T> {

        int index;
        Entry<K, V> entry = null;
        Entry<K, V> lastReturned = null;
        int expectedModCount = modCount;
        Object nextKey = null;
        Object currentKey = null;

        HashIterator() {
            index = (size() != 0 ? table.length : 0);
        }

        @Override
        public boolean hasNext() {
            final Entry<K, V>[] t = table;

            while (nextKey == null) {
                Entry<K, V> e = entry;
                int i = index;
                while (e == null && i > 0) {
                    e = t[--i];
                }
                entry = e;
                index = i;
                if (e == null) {
                    currentKey = null;
                    return false;
                }
                nextKey = e.get(); // hold on to key in strong ref
                if (nextKey == null) {
                    entry = entry.next;
                }
            }
            return true;
        }

        @Override
        public void remove() {
            if (lastReturned == null) {
                throw new IllegalStateException();
            }
            if (modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }

            SoftHashMap.this.remove(currentKey);
            expectedModCount = modCount;
            lastReturned = null;
            currentKey = null;
        }

        protected Entry<K, V> nextEntry() {
            if (modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }
            if (nextKey == null && !hasNext()) {
                throw new NoSuchElementException();
            }

            lastReturned = entry;
            entry = entry.next;
            currentKey = nextKey;
            nextKey = null;
            return lastReturned;
        }
    }

    private class ValueIterator extends HashIterator<V> {

        @Override
        public V next() {
            return nextEntry().value;
        }
    }

    private class KeyIterator extends HashIterator<K> {

        @Override
        public K next() {
            return nextEntry().getKey();
        }
    }

    private class EntryIterator extends HashIterator<Map.Entry<K, V>> {

        @Override
        public Map.Entry<K, V> next() {
            return nextEntry();
        }
    }

    private class KeySet extends AbstractSet<K> {

        @Override
        public Iterator<K> iterator() {
            return new KeyIterator();
        }

        @Override
        public int size() {
            return SoftHashMap.this.size();
        }

        @Override
        public boolean contains(final Object o) {
            return containsKey(o);
        }

        @Override
        public boolean remove(final Object o) {
            if (containsKey(o)) {
                SoftHashMap.this.remove(o);
                return true;
            } else {
                return false;
            }
        }

        @Override
        public void clear() {
            SoftHashMap.this.clear();
        }
    }

    private class Values extends AbstractCollection<V> {

        @Override
        public Iterator<V> iterator() {
            return new ValueIterator();
        }

        @Override
        public int size() {
            return SoftHashMap.this.size();
        }

        @Override
        public boolean contains(final Object o) {
            return containsValue(o);
        }

        @Override
        public void clear() {
            SoftHashMap.this.clear();
        }
    }

    private class EntrySet extends AbstractSet<Map.Entry<K, V>> {

        @Override
        public Iterator<Map.Entry<K, V>> iterator() {
            return new EntryIterator();
        }

        @Override
        public int size() {
            return SoftHashMap.this.size();
        }

        @Override
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            final Map.Entry<?, ?> e = (Map.Entry<?, ?>) o;
            final Entry<K, V> candidate = getEntry(e.getKey());
            return candidate != null && candidate.equals(e);
        }

        @Override
        public Object[] toArray() {
            return deepCopy().toArray();
        }

        @Override
        public <T> T[] toArray(final T[] a) {
            return deepCopy().toArray(a);
        }

        @Override
        public boolean remove(final Object o) {
            return removeMapping(o) != null;
        }

        @Override
        public void clear() {
            SoftHashMap.this.clear();
        }

        private List<Map.Entry<K, V>> deepCopy() {
            final List<Map.Entry<K, V>> list = new ArrayList<>(size());
            for (final Map.Entry<K, V> e : this) {
                list.add(new SimpleEntry<K, V>(e));
            }
            return list;
        }
    }
}
