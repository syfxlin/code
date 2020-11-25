package me.ixk.days.day6;

import static org.junit.jupiter.api.Assertions.assertEquals;

import me.ixk.days.day6.aop.AspectManager;
import me.ixk.days.day6.aop.AspectPointcut;
import me.ixk.days.day6.aop.ProxyCreator;
import me.ixk.days.day6.aspect.LogAspect;
import org.junit.jupiter.api.Test;

/**
 * @author Otstar Lin
 * @date 2020/11/25 下午 7:09
 */
class Day6Test {

    @Test
    void aspect() {
        final AspectManager manager = new AspectManager();
        // 添加切面
        manager.addAdvice(
            new AspectPointcut("@annotation(me.ixk.days.day6.annotation.Log)"),
            new LogAspect()
        );
        final TestMethod testMethod = (TestMethod) ProxyCreator.createAop(
            manager,
            new TestMethod(),
            TestMethod.class,
            new Class[0],
            new Class[0],
            new Object[0]
        );
        assertEquals("BeforeMethodAfter", testMethod.index());
    }
}
