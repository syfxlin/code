package me.ixk.design_pattern.factory.factory_method;

import me.ixk.design_pattern.factory.animal.Cat;

/**
 * @author Otstar Lin
 * @date 2020/12/22 上午 11:26
 */
public class CatFactory implements FactoryMethod<Cat> {

    @Override
    public Cat createAnimal() {
        return new Cat();
    }
}
