package me.ixk.days.day29;

import lombok.extern.slf4j.Slf4j;

/**
 * 子类一
 *
 * @author Otstar Lin
 * @date 2020/12/20 下午 2:51
 */
@Slf4j
@SuppressWarnings("rawtypes")
public class Child1 extends Parent {

    @SuppressWarnings("unchecked")
    public void setValue(final String value) {
        log.info("Child set value");
        super.setValue(value);
    }
}
