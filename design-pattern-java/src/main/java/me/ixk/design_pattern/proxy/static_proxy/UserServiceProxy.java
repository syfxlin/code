package me.ixk.design_pattern.proxy.static_proxy;

import lombok.extern.slf4j.Slf4j;
import me.ixk.design_pattern.proxy.UserService;

/**
 * @author Otstar Lin
 * @date 2020/12/26 下午 1:39
 */
@Slf4j
public class UserServiceProxy extends UserService {

    @Override
    public String getUsername() {
        log.info("执行被代理方法前");
        final String result = super.getUsername();
        log.info("执行被代理方法后");
        log.info("返回值：{}", result);
        return result + "proxy";
    }
}
