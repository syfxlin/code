package me.ixk.design_pattern.factory.factory_method;

import me.ixk.design_pattern.factory.animal.Dog;

/**
 * @author Otstar Lin
 * @date 2020/12/22 上午 11:26
 */
public class DogFactory implements FactoryMethod<Dog> {

    @Override
    public Dog createAnimal() {
        return new Dog();
    }
}
