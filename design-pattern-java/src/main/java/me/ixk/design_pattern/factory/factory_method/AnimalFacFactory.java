package me.ixk.design_pattern.factory.factory_method;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Otstar Lin
 * @date 2020/12/22 上午 11:26
 */
public class AnimalFacFactory {

    private static final Map<Class<?>, FactoryMethod<?>> factoryMap = new ConcurrentHashMap<>();

    static {
        factoryMap.put(CatFactory.class, new CatFactory());
        factoryMap.put(DogFactory.class, new DogFactory());
    }

    public <T extends FactoryMethod<?>> T createFactory(final Class<T> type) {
        return type.cast(factoryMap.get(type));
    }
}
