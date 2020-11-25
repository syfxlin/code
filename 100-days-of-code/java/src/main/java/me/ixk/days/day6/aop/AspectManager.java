/*
 * Copyright (c) 2020, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.days.day6.aop;

import cn.hutool.core.lang.SimpleCache;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import me.ixk.days.day5.AnnotationUtils;
import me.ixk.days.day6.annotation.Order;

/**
 * 切面管理器
 * <p>
 * 管理切面的匹配和获取
 *
 * @author Otstar Lin
 * @date 2020/10/14 上午 8:25
 */
public class AspectManager {
    /**
     * 所有的切面列表
     */
    private final List<AdviceEntry> adviceList = new ArrayList<>();

    private final SimpleCache<Method, List<Advice>> METHOD_CACHE = new SimpleCache<>();

    public void addAdvice(AspectPointcut pointcut, Advice advice) {
        adviceList.add(new AdviceEntry(pointcut, advice));
    }

    private static class AdviceEntry {
        private final AspectPointcut pointcut;

        private final Advice advice;

        private final int order;

        public AdviceEntry(AspectPointcut pointcut, Advice advice) {
            this.pointcut = pointcut;
            this.advice = advice;
            Integer order = AnnotationUtils
                .getAnnotation(advice.getClass())
                .get(Order.class, "order");
            this.order =
                Objects.requireNonNullElse(order, Order.LOWEST_PRECEDENCE);
        }

        public AspectPointcut getPointcut() {
            return pointcut;
        }

        public Advice getAdvice() {
            return advice;
        }

        public int getOrder() {
            return order;
        }
    }

    public boolean matches(Class<?> clazz) {
        for (AdviceEntry entry : adviceList) {
            if (entry.getPointcut().matches(clazz)) {
                return true;
            }
        }
        return false;
    }

    public boolean matches(Method method) {
        for (AdviceEntry entry : adviceList) {
            if (entry.getPointcut().matches(method)) {
                return true;
            }
        }
        return false;
    }

    public List<Advice> getAdvices(Method method) {
        List<Advice> cache = METHOD_CACHE.get(method);
        if (cache != null) {
            return cache;
        }
        List<Advice> list = new ArrayList<>();
        for (AdviceEntry entry : adviceList) {
            if (entry.getPointcut().matches(method)) {
                list.add(entry.getAdvice());
            }
        }
        METHOD_CACHE.put(method, list);
        return list;
    }
}
