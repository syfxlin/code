package me.ixk.design_pattern.iterator;

/**
 * 迭代器接口
 * <p>
 * 通过调用 hasNext 判断是否还有下一个元素，当有下一个元素的时候就使用 next 取得下一个元素
 * <p>
 * 在 Java 中有 Iterator 的接口，为了方便展示这里就写一个简单的代替
 *
 * @param <E> 迭代元素的类型
 */
public interface Iterator<E> {
    boolean hasNext();

    E next();
}
