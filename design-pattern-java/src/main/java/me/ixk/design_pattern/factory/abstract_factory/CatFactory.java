package me.ixk.design_pattern.factory.abstract_factory;

import me.ixk.design_pattern.factory.animal.Cat;

/**
 * @author Otstar Lin
 * @date 2020/12/22 上午 11:27
 */
public class CatFactory implements AbstractFactory<Cat> {

    @Override
    public Cat createAnimal() {
        return new Cat();
    }

    @Override
    public String createAnimalFood() {
        return "cat food";
    }
}
