package me.ixk.days.day24;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

/**
 * @author Otstar Lin
 * @date 2020/12/15 上午 8:25
 */
class Day24Test {

    @Test
    void equals() {
        // Integer.valueOf(1)
        final Integer a1 = 1;
        final Integer b1 = 1;
        // 包装类在 [-128, 127] 有缓存，通过 Integer.valueOf 获取到同一个值的包装类其实就是获取同一个缓存，所以用 == 比较是相等的
        assertSame(a1, b1);
        assertEquals(a1, b1);

        final Integer a2 = 1000;
        final Integer b2 = 1000;
        // 超出了缓存范围，此时 Integer.valueOf 返回的包装类对象是不能使用 == 比较的，因为都是不一样的对象
        assertNotSame(a2, b2);
        assertEquals(a2, b2);

        final Integer a3 = 1;
        final Integer b3 = new Integer(1);
        // 使用 new Integer 创建的对象是无法使用缓存的，所以也不能用 == 比较
        assertNotSame(a3, b3);
        assertEquals(a3, b3);

        final Integer a4 = new Integer(1);
        final Integer b4 = new Integer(1);
        // 同上
        assertNotSame(a4, b4);
        assertEquals(a4, b4);

        final Integer a5 = 1;
        final int b5 = 1;
        // 如果是包装类和基本类型进行比较，那么会先拆包再比较，此时基本类型就可以使用 == 比较了
        assertSame(a5, b5);
        assertEquals(a5, b5);
        // 总结：任何对象都不应使用 == 进行判等，应使用 equals，同时该对象的类应重写 equals。
    }

    @Test
    void map() {
        final Map<User, Integer> s1 = new HashMap<>();
        s1.put(new User("123", 1), 1);
        s1.put(new User("123", 1), 2);
        assertEquals(1, s1.size());

        final Map<UserNotHashCode, Integer> s2 = new HashMap<>();
        // Map 和 Set 会先计算 hashCode 然后使用 equals，最终找到唯一的位置
        // 如果没有重写 equals，则会导致原本应该是看作同一对象的对象无法在 Set 中唯一
        s2.put(new UserNotHashCode("123", 1), 1);
        s2.put(new UserNotHashCode("123", 1), 2);
        assertEquals(2, s2.size());

        final Map<UserNotEquals, Integer> s3 = new HashMap<>();
        // 只有 hashCode 也不行
        s3.put(new UserNotEquals("123", 1), 1);
        s3.put(new UserNotEquals("123", 1), 2);
        assertEquals(2, s3.size());

        final Map<UserNotEqualsAndHashCode, Integer> s4 = new HashMap<>();
        s4.put(new UserNotEqualsAndHashCode("123", 1), 1);
        s4.put(new UserNotEqualsAndHashCode("123", 1), 2);
        assertEquals(2, s4.size());
    }
}
