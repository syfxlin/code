package me.ixk.design_pattern.factory.animal;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Dog implements Animal {

    @Override
    public void eat() {
        log.info("Dog eat");
    }

    @Override
    public void sleep() {
        log.info("Dog sleep");
    }
}
