package me.ixk.design_pattern.factory;

import me.ixk.design_pattern.factory.animal.Animal;
import me.ixk.design_pattern.factory.animal.Cat;
import me.ixk.design_pattern.factory.animal.Dog;

/**
 * 简单工厂
 * <p>
 * 优点：简单易用
 * <p>
 * 缺点：当工厂需要制作新的类型的实例的时候需要修改工厂的代码
 */
public class SimpleFactory {

    public <T extends Animal> T makeAnimal(final Class<T> animalType) {
        final Animal animal;
        if (animalType == Cat.class) {
            animal = new Cat();
        } else {
            animal = new Dog();
        }
        return animalType.cast(animal);
    }
}
