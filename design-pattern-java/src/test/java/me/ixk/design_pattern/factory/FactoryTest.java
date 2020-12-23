package me.ixk.design_pattern.factory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import me.ixk.design_pattern.factory.abstract_factory.AbstractFactory;
import me.ixk.design_pattern.factory.abstract_factory.CatFactory;
import me.ixk.design_pattern.factory.animal.Cat;
import me.ixk.design_pattern.factory.animal.Dog;
import me.ixk.design_pattern.factory.factory_method.AnimalFacFactory;
import me.ixk.design_pattern.factory.factory_method.FactoryMethod;
import org.junit.jupiter.api.Test;

public class FactoryTest {

    @Test
    void simple() {
        SimpleFactory factory = new SimpleFactory();
        assertSame(Cat.class, factory.makeAnimal(Cat.class).getClass());
        assertSame(Dog.class, factory.makeAnimal(Dog.class).getClass());
    }

    @Test
    void factoryMethod() {
        FactoryMethod<Cat> catFactory = new me.ixk.design_pattern.factory.factory_method.CatFactory();
        assertSame(Cat.class, catFactory.createAnimal().getClass());

        AnimalFacFactory facFactory = new AnimalFacFactory();

        assertSame(
            Dog.class,
            facFactory
                .createFactory(
                    me.ixk.design_pattern.factory.factory_method.DogFactory.class
                )
                .createAnimal()
                .getClass()
        );
    }

    @Test
    void abstractFactory() {
        AbstractFactory<Cat> catFactory = new CatFactory();
        assertSame(Cat.class, catFactory.createAnimal().getClass());
        assertEquals("cat food", catFactory.createAnimalFood());
    }
}
