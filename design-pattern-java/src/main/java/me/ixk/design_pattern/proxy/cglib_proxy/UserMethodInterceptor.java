package me.ixk.design_pattern.proxy.cglib_proxy;

import java.lang.reflect.Method;
import me.ixk.design_pattern.proxy.UserService;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * @author Otstar Lin
 * @date 2020/12/26 下午 1:58
 */
public class UserMethodInterceptor implements MethodInterceptor {

    UserService userService;

    public UserMethodInterceptor(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Object intercept(
        Object obj,
        Method method,
        Object[] args,
        MethodProxy proxy
    ) throws Throwable {
        System.out.println("执行被代理方法前");
        Object result = proxy.invoke(this.userService, args);
        System.out.println("执行被代理方法后");
        System.out.println("返回值: " + result);
        return result + "proxy";
    }
}
