package me.ixk.design_pattern.singleton;

import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

public class SingletonTest {

    @Test
    void simple() {
        assertSame(
            SimpleSingleton.getInstance(),
            SimpleSingleton.getInstance()
        );
    }

    @Test
    void lazy() {
        assertSame(LazySingleton.getInstance(), LazySingleton.getInstance());
    }

    @Test
    void dcl() {
        assertSame(DclSingleton.getInstance(), DclSingleton.getInstance());
    }

    @Test
    void staticClass() {
        assertSame(
            StaticClassSingleton.getInstance(),
            StaticClassSingleton.getInstance()
        );
    }

    @Test
    void enumT() {
        assertSame(EnumSingleton.INSTANCE, EnumSingleton.INSTANCE);
    }

    @Test
    void container() {
        ContainerSingleton.register("string");

        assertSame(
            ContainerSingleton.get(String.class),
            ContainerSingleton.get(String.class)
        );
    }
}
