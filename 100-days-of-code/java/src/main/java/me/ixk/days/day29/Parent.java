package me.ixk.days.day29;

import lombok.extern.slf4j.Slf4j;

/**
 * 父类
 *
 * @author Otstar Lin
 * @date 2020/12/20 下午 2:48
 */
@Slf4j
@Anno
public class Parent<T> {

    private int count;
    private T value;

    @Anno
    public void setValue(final T value) {
        log.info("Parent set value");
        this.count++;
        this.value = value;
    }

    public int getCount() {
        return count;
    }

    public T getValue() {
        return value;
    }
}
