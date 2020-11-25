package me.ixk.days.day6.aspect;

import lombok.extern.slf4j.Slf4j;
import me.ixk.days.day6.aop.AbstractAdvice;
import me.ixk.days.day6.aop.ProceedingJoinPoint;

/**
 * @author Otstar Lin
 * @date 2020/11/25 下午 7:08
 */
@Slf4j
public class LogAspect extends AbstractAdvice {

    @Override
    public Object around(final ProceedingJoinPoint joinPoint) {
        log.info("Before-Log");
        String result = "Before";
        try {
            result = result + joinPoint.proceed();
        } catch (final Throwable throwable) {
            log.error("Error", throwable);
        }
        log.info("After-Log");
        result += "After";
        return result;
    }
}
