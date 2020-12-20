package me.ixk.days.day29;

import lombok.extern.slf4j.Slf4j;

/**
 * 子类二
 *
 * @author Otstar Lin
 * @date 2020/12/20 下午 2:53
 */
@Slf4j
public class Child2 extends Parent<String> {

    @Override
    public void setValue(final String value) {
        log.info("Child set value");
        super.setValue(value);
    }
}
