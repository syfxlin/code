package me.ixk.design_pattern.factory.factory_method;

import me.ixk.design_pattern.factory.animal.Animal;

/**
 * 简单工厂：通过多态来拆分简单工厂的 if 判断
 * <p>
 * 优点：当需要增加新的实例的时候无效修改原始代码，只需要另外创建一个对应的工厂
 * <p>
 * 缺点：工厂会因需要创建的类越来越多而逐渐增多（此时可以通过创建一个工厂的简单工厂来解决这个问题）
 */
public interface FactoryMethod<T extends Animal> {
    T createAnimal();
}
