package me.ixk.design_pattern.factory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import me.ixk.design_pattern.factory.animal.Animal;
import me.ixk.design_pattern.factory.animal.Cat;
import me.ixk.design_pattern.factory.animal.Dog;

/**
 * 简单工厂：通过多态来拆分简单工厂的 if 判断
 * <p>
 * 优点：当需要增加新的实例的时候无效修改原始代码，只需要另外创建一个对应的工厂
 * <p>
 * 缺点：工厂会因需要创建的类越来越多而逐渐增多（此时可以通过创建一个工厂的简单工厂来解决这个问题）
 */
interface FactoryMethod<T extends Animal> {
    T createAnimal();
}

class CatFactory implements FactoryMethod<Cat> {

    @Override
    public Cat createAnimal() {
        return new Cat();
    }
}

class DogFactory implements FactoryMethod<Dog> {

    @Override
    public Dog createAnimal() {
        return new Dog();
    }
}

class AnimalFacFactory {
    private static final Map<Class<?>, FactoryMethod<?>> factoryMap = new ConcurrentHashMap<>();

    static {
        factoryMap.put(CatFactory.class, new CatFactory());
        factoryMap.put(DogFactory.class, new DogFactory());
    }

    public <T extends FactoryMethod<?>> T createFactory(Class<T> type) {
        return type.cast(factoryMap.get(type));
    }
}
