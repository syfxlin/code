package me.ixk.design_pattern.proxy;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Proxy;
import me.ixk.design_pattern.proxy.cglib_proxy.UserMethodInterceptor;
import me.ixk.design_pattern.proxy.jdk_proxy.UserInvocationHandler;
import me.ixk.design_pattern.proxy.static_proxy.UserServiceProxy;
import net.sf.cglib.proxy.Enhancer;
import org.junit.jupiter.api.Test;

/**
 * @author Otstar Lin
 * @date 2020/12/26 下午 1:43
 */
class ProxyTest {

    @Test
    void staticProxy() {
        final UserService proxy = new UserServiceProxy();
        assertEquals("syfxlinproxy", proxy.getUsername());
    }

    @Test
    void jdkProxy() {
        UserService userService = new UserService();
        UserServiceInterface proxy = (UserServiceInterface) Proxy.newProxyInstance(
            ProxyTest.class.getClassLoader(),
            UserService.class.getInterfaces(),
            new UserInvocationHandler(userService)
        );
        assertEquals("syfxlinproxy", proxy.getUsername());
    }

    @Test
    void cglibProxy() {
        final UserService userService = new UserService();
        UserService proxy = (UserService) Enhancer.create(
            UserService.class,
            UserService.class.getInterfaces(),
            new UserMethodInterceptor(userService)
        );
        assertEquals("syfxlinproxy", proxy.getUsername());
    }
}
