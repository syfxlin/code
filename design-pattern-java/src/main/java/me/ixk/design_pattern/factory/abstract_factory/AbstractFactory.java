package me.ixk.design_pattern.factory.abstract_factory;

import me.ixk.design_pattern.factory.animal.Animal;

/**
 * 抽象工厂
 *
 * @author Otstar Lin
 * @date 2020/12/22 上午 11:21
 */
public interface AbstractFactory<T extends Animal> {
    T createAnimal();

    String createAnimalFood();
}
