package me.ixk.days.day29;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

/**
 * @author Otstar Lin
 * @date 2020/12/20 下午 2:43
 */
class Day29Test {

    @Test
    void overload()
        throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // 使用反射的方式调用包装类型方法和基本类型方法需要手动指定，无法自动选择
        final Overload overload = new Overload();
        final Method m1 = overload.getClass().getMethod("age", int.class);
        final int a1 = (int) m1.invoke(overload, 1);
        assertEquals(2, a1);
        final Method m2 = overload.getClass().getMethod("age", Integer.class);
        final int a2 = (int) m2.invoke(overload, 1);
        assertEquals(3, a2);

        assertEquals(2, overload.age(1));
        assertEquals(3, overload.age(Integer.valueOf(1)));
    }

    @Test
    void child1() throws InvocationTargetException, IllegalAccessException {
        final Child1 child1 = new Child1();
        for (final Method method : child1.getClass().getMethods()) {
            if (method.getName().equals("setValue")) {
                method.invoke(child1, "value");
            }
        }
        // 由于并未指定 Parent 的泛型，所以父类的方法签名其实是 setValue(Object)，而子类的是 setValue(String) 此时并没有重载，所以会出现两个方法
        assertEquals(2, child1.getCount());
    }

    @Test
    void child2() throws InvocationTargetException, IllegalAccessException {
        final Child2 child2 = new Child2();
        for (final Method method : child2.getClass().getMethods()) {
            if (method.getName().equals("setValue")) {
                method.invoke(child2, "value");
            }
        }
        // 即使指定了 Parent 的泛型，也会生成一个桥接方法，将 setValue(Object) 桥接到 setValue(String) 所以也会出现两个方法
        assertEquals(2, child2.getCount());
    }

    @Test
    void child2SkipBridge()
        throws InvocationTargetException, IllegalAccessException {
        final Child2 child2 = new Child2();
        for (final Method method : child2.getClass().getMethods()) {
            // 为了防止这种问题，则需要判断是否是桥接方法，如果是则不操作
            if (method.getName().equals("setValue") && !method.isBridge()) {
                method.invoke(child2, "value");
            }
        }
        assertEquals(1, child2.getCount());
    }

    @Test
    void annotation() throws NoSuchMethodException {
        final Child2 child2 = new Child2();
        final Anno a1 = child2.getClass().getAnnotation(Anno.class);
        // 类可以集成父类的注解
        assertNotNull(a1);
        final Anno a2 = child2
            .getClass()
            .getMethod("setValue", String.class)
            .getAnnotation(Anno.class);
        // 方法无法继承父类的注解
        assertNull(a2);
    }
}
