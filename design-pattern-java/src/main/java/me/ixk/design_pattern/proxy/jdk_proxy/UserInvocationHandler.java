package me.ixk.design_pattern.proxy.jdk_proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import me.ixk.design_pattern.proxy.UserServiceInterface;

/**
 * @author Otstar Lin
 * @date 2020/12/26 下午 1:49
 */
public class UserInvocationHandler implements InvocationHandler {

    final UserServiceInterface userService;

    public UserInvocationHandler(final UserServiceInterface userService) {
        this.userService = userService;
    }

    @Override
    public Object invoke(
        final Object proxy,
        final Method method,
        final Object[] args
    ) throws Throwable {
        System.out.println("执行被代理方法前");
        final String result = (String) method.invoke(this.userService, args);
        System.out.println("执行被代理方法后");
        System.out.println("返回值: " + result);
        return result + "proxy";
    }
}
