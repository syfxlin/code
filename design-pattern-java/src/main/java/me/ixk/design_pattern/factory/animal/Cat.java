package me.ixk.design_pattern.factory.animal;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Cat implements Animal {

    @Override
    public void eat() {
        log.info("Cat eat");
    }

    @Override
    public void sleep() {
        log.info("Cat sleep");
    }
}
