package me.ixk.days.day16;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * @author Otstar Lin
 * @date 2020/12/7 下午 9:43
 */
class Day16Test {

    @Test
    void make() throws NoSuchMethodException {
        final Container container = new Container();
        container.singleton("name", (c, w) -> "name");
        container.singleton(Test1.class, Test1.class);
        container.singleton(Test2.class, Test2.class);
        final Test2 test2 = container.make(Test2.class);
        assertEquals("name", test2.getTest1().getName());
        assertEquals(
            "name",
            container.call(Day16Test.class.getMethod("call", Test1.class))
        );
    }

    public String call(Test1 test1) {
        return test1.getName();
    }

    public static class Test1 {

        private final String name;

        public Test1(final String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public static class Test2 {

        private final Test1 test1;

        public Test2(final Test1 test1) {
            this.test1 = test1;
        }

        public Test1 getTest1() {
            return test1;
        }
    }
}
