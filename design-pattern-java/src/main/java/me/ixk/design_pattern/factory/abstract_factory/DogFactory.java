package me.ixk.design_pattern.factory.abstract_factory;

import me.ixk.design_pattern.factory.animal.Dog;

/**
 * @author Otstar Lin
 * @date 2020/12/22 上午 11:27
 */
public class DogFactory implements AbstractFactory<Dog> {

    @Override
    public Dog createAnimal() {
        return new Dog();
    }

    @Override
    public String createAnimalFood() {
        return "dog food";
    }
}
