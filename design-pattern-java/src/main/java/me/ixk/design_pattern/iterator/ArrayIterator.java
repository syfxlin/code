package me.ixk.design_pattern.iterator;

/**
 * 具体的迭代器
 */
public class ArrayIterator implements Iterator<Integer> {
    private final int[] arr;
    private int index;

    public ArrayIterator(int[] arr) {
        this.arr = arr;
        this.index = 0;
    }

    @Override
    public boolean hasNext() {
        return this.index < arr.length;
    }

    @Override
    public Integer next() {
        if (this.hasNext()) {
            return this.arr[this.index++];
        } else {
            // return null or throw exception
            return null;
        }
    }
}
