package me.ixk.design_pattern.observer;

import org.junit.jupiter.api.Test;

/**
 * @author Otstar Lin
 * @date 2021/1/2 下午 8:56
 */
class ObserverTest {

    @Test
    void observer() {
        final EmailServer server = new EmailServer();
        server.attach(new UserClient("Phone"));
        server.attach(new UserClient("Laptop"));
        server.send("39352");
    }
}
