package me.ixk.days.day19;

/**
 * 封装版本号的引用
 * <p>
 * 不可变类，原生支持多线程
 *
 * @author Otstar Lin
 * @date 2020/12/10 上午 10:28
 */
public class VersionedRef<V> {

    private final V value;
    private final long version;

    public VersionedRef(final V value, final long version) {
        this.value = value;
        this.version = version;
    }

    public V getValue() {
        return value;
    }

    public long getVersion() {
        return version;
    }
}
