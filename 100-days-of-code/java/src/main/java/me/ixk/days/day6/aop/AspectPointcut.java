/*
 * Copyright (c) 2020, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.days.day6.aop;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import org.aspectj.weaver.tools.PointcutExpression;
import org.aspectj.weaver.tools.PointcutParser;
import org.aspectj.weaver.tools.PointcutPrimitive;
import org.aspectj.weaver.tools.ShadowMatch;

/**
 * 切面匹配器
 * <p>
 * 使用 AspectJ 来进行切面表达式的匹配
 *
 * @author Otstar Lin
 * @date 2020/10/14 上午 8:27
 */
public class AspectPointcut {
    private static final Set<PointcutPrimitive> POINTCUT_PRIMITIVES = new HashSet<>();

    static {
        POINTCUT_PRIMITIVES.add(PointcutPrimitive.EXECUTION);
        POINTCUT_PRIMITIVES.add(PointcutPrimitive.ARGS);
        POINTCUT_PRIMITIVES.add(PointcutPrimitive.REFERENCE);
        POINTCUT_PRIMITIVES.add(PointcutPrimitive.THIS);
        POINTCUT_PRIMITIVES.add(PointcutPrimitive.TARGET);
        POINTCUT_PRIMITIVES.add(PointcutPrimitive.WITHIN);
        POINTCUT_PRIMITIVES.add(PointcutPrimitive.AT_ANNOTATION);
        POINTCUT_PRIMITIVES.add(PointcutPrimitive.AT_WITHIN);
        POINTCUT_PRIMITIVES.add(PointcutPrimitive.AT_ARGS);
        POINTCUT_PRIMITIVES.add(PointcutPrimitive.AT_TARGET);
    }

    private final PointcutParser pointcutParser;
    private final PointcutExpression pointcutExpression;

    public AspectPointcut(String expression) {
        this(expression, POINTCUT_PRIMITIVES);
    }

    public AspectPointcut(
        String expression,
        Set<PointcutPrimitive> pointcutPrimitives
    ) {
        this.pointcutParser =
            PointcutParser.getPointcutParserSupportingSpecifiedPrimitivesAndUsingContextClassloaderForResolution(
                pointcutPrimitives
            );
        this.pointcutExpression =
            this.pointcutParser.parsePointcutExpression(expression);
    }

    public boolean matches(Class<?> target) {
        return this.pointcutExpression.couldMatchJoinPointsInType(target);
    }

    public boolean matches(Method method) {
        ShadowMatch match =
            this.pointcutExpression.matchesMethodExecution(method);
        if (match.alwaysMatches()) {
            return true;
        } else if (match.neverMatches()) {
            return false;
        }
        return false;
    }
}
