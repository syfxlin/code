package me.ixk.days.day19;

/**
 * 事务引用
 *
 * @author Otstar Lin
 * @date 2020/12/10 上午 10:29
 */
public class TransactionRef<V> {

    private volatile VersionedRef<V> ref;

    public TransactionRef() {
        this(null);
    }

    public TransactionRef(final V value) {
        this.ref = new VersionedRef<>(value, 0L);
    }

    public V get(final Transaction tr) {
        return tr.get(this);
    }

    public void set(final Transaction tr, final V value) {
        tr.set(this, value);
    }

    public VersionedRef<V> getRef() {
        return ref;
    }

    public void setRef(final VersionedRef<V> ref) {
        this.ref = ref;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        return super.equals(obj);
    }
}
