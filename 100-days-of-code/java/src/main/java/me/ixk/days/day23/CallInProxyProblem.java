package me.ixk.days.day23;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * 在代理类里调用同类方法增强不生效问题
 * <p>
 * 可以看到，即使使用了代理，也无法是代理 call1 生效，这是因为 this.call1() 并不会经过代理类，而是直接调用了被代理类
 * <p>
 * 该问题有可能会在 Spring 事务上常见到，比如我们开启了事务，但是又通过 this 调用另一个想开启事务的方法，此时就无法通过 Aop
 * 代理启动嵌套事务了。
 * <p>
 * Proxy.call2() -> Interceptor -> Target.call2() -> Target.call1()
 *
 * @author Otstar Lin
 * @date 2020/12/14 下午 1:51
 */
@Slf4j
public class CallInProxyProblem implements CallInProxy {

    @Override
    public String call1() {
        return "call1";
    }

    @Override
    public String call2() {
        return this.call1() + "call2";
    }

    public static class CallInProxyProblemInterceptor
        implements MethodInterceptor, InvocationHandler {

        private final CallInProxy target;

        public CallInProxyProblemInterceptor(CallInProxy target) {
            this.target = target;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {
            final Object result = method.invoke(this.target, args);
            return result + "proxy";
        }

        @Override
        public Object intercept(
            Object obj,
            Method method,
            Object[] args,
            MethodProxy proxy
        ) throws Throwable {
            final Object result = proxy.invoke(this.target, args);
            return result + "proxy";
        }
    }
}
