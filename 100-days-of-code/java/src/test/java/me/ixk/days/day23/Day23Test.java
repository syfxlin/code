package me.ixk.days.day23;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Proxy;
import lombok.extern.slf4j.Slf4j;
import me.ixk.days.day23.CallInProxyProblem.CallInProxyProblemInterceptor;
import me.ixk.days.day23.CallInProxySolve.CallInProxySolveInterceptor;
import net.sf.cglib.proxy.Enhancer;
import org.junit.jupiter.api.Test;

/**
 * @author Otstar Lin
 * @date 2020/12/14 下午 2:00
 */
@Slf4j
class Day23Test {

    @Test
    void problem() {
        CallInProxy problem = new CallInProxyProblem();
        final CallInProxy problemProxy = (CallInProxy) Proxy.newProxyInstance(
            Day23Test.class.getClassLoader(),
            new Class[] { CallInProxy.class },
            new CallInProxyProblemInterceptor(problem)
        );
        assertEquals("call1proxy", problemProxy.call1());
        assertEquals("call1call2proxy", problemProxy.call2());

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(CallInProxyProblem.class);
        enhancer.setCallback(new CallInProxyProblemInterceptor(problem));
        final CallInProxy problemCglib = (CallInProxy) enhancer.create();

        assertEquals("call1proxy", problemCglib.call1());
        assertEquals("call1call2proxy", problemCglib.call2());
    }

    @Test
    void solve() {
        CallInProxy solve = new CallInProxySolve();
        final CallInProxy solveProxy = (CallInProxy) Proxy.newProxyInstance(
            Day23Test.class.getClassLoader(),
            new Class[] { CallInProxy.class },
            new CallInProxySolveInterceptor(solve)
        );
        solveProxy.setSelf(solveProxy);
        assertEquals("call1proxy", solveProxy.call1());
        assertEquals("call1proxycall2proxy", solveProxy.call2());

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(CallInProxySolve.class);
        enhancer.setCallback(new CallInProxySolveInterceptor(solve));
        final CallInProxy solveCglib = (CallInProxy) enhancer.create();

        assertEquals("call1proxy", solveCglib.call1());
        assertEquals("call1proxycall2proxy", solveCglib.call2());
    }
}
