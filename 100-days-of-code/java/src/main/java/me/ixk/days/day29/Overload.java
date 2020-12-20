package me.ixk.days.day29;

/**
 * 反射调用重载方法问题
 *
 * @author Otstar Lin
 * @date 2020/12/20 下午 2:42
 */
public class Overload {

    public int age(int age) {
        return age + 1;
    }

    public int age(Integer age) {
        return age + 2;
    }
}
