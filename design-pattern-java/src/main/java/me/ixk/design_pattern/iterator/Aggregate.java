package me.ixk.design_pattern.iterator;

/**
 * 集合
 * <p>
 * 用于创建访问某种集合元素中的迭代器
 * <p>
 * 在 Java 中对应 Iterable
 *
 * @param <E>
 */
public interface Aggregate<E> {
    Iterator<E> iterator();
}
