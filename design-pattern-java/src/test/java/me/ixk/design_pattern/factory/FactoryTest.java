package me.ixk.design_pattern.factory;

import static org.junit.jupiter.api.Assertions.assertSame;

import me.ixk.design_pattern.factory.animal.Cat;
import me.ixk.design_pattern.factory.animal.Dog;
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
        FactoryMethod<Cat> catFactory = new CatFactory();
        assertSame(Cat.class, catFactory.createAnimal().getClass());

        AnimalFacFactory facFactory = new AnimalFacFactory();

        assertSame(
            Dog.class,
            facFactory.createFactory(DogFactory.class).createAnimal().getClass()
        );
    }
}
