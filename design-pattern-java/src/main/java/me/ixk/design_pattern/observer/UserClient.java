package me.ixk.design_pattern.observer;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Otstar Lin
 * @date 2021/1/2 下午 8:54
 */
@Slf4j
public class UserClient implements Observer {

    private final String name;

    public UserClient(String name) {
        this.name = name;
    }

    @Override
    public void change(Object data, Subject subject) {
        log.info("[{}] 有新邮件啦：这是一封验证码邮件: {}", this.name, data);
    }
}
