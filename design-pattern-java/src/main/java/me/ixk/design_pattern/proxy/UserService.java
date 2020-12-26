package me.ixk.design_pattern.proxy;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Otstar Lin
 * @date 2020/12/26 下午 1:36
 */
@Slf4j
public class UserService implements UserServiceInterface {

    @Override
    public String getUsername() {
        log.info("执行被代理方法中");
        return "syfxlin";
    }
}
