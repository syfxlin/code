package me.ixk.days.day23;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * 在代理类里调用同类方法增强不生效的解决
 * <p>
 * 为了解决这种问题，就不应该使用 this 引用调用内部方法，而应该传入代理对象，调用这个代理对象，这样代理就能生效了。
 * <p>
 * Proxy.call2() -> Interceptor -> Target.call2() -> Proxy(self).call1() ->
 * Interceptor -> Target.call1()
 *
 * @author Otstar Lin
 * @date 2020/12/14 下午 2:10
 */
public class CallInProxySolve implements CallInProxy {

    private CallInProxy self;

    @Override
    public void setSelf(CallInProxy self) {
        this.self = self;
    }

    @Override
    public String call1() {
        return "call1";
    }

    @Override
    public String call2() {
        return self.call1() + "call2";
    }

    public static class CallInProxySolveInterceptor
        implements MethodInterceptor, InvocationHandler {

        private final CallInProxy target;

        public CallInProxySolveInterceptor(CallInProxy target) {
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
