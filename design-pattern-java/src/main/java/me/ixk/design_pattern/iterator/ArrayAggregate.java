package me.ixk.design_pattern.iterator;

/**
 * 具体的集合
 */
public class ArrayAggregate implements Aggregate<Integer> {

    @Override
    public Iterator<Integer> iterator() {
        return new ArrayIterator(new int[] { 1, 2, 3, 4, 5 });
    }
}
