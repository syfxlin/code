package me.ixk.days.day8;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

/**
 * @author Otstar Lin
 * @date 2020/11/28 下午 7:22
 */
class Day8Test {

    @Test
    void route() throws NoSuchMethodException {
        final RouteCollector collector = new RouteCollector(
            new RouteParser(),
            new RouteGenerator()
        );
        final RouteDispatcher dispatcher = new RouteDispatcher(collector);
        final Method handler = this.getClass().getMethod("handler");
        collector.get("/{id?:[0-9]+}", handler);
        collector.get("/user1/{id}", handler);
        collector.get("/user2/{id?}", handler);
        collector.get("/user3/{id:[a-z]+}", handler);
        collector.get("/user4/{id?:[a-z]+}", handler);
        collector.get("/user5/{id}/{name}", handler);
        collector.get("/user6/name", handler);
        collector.get("/user7/name/{name}", handler);
        collector.get("/user8/n{.}me", handler);
        collector.get("/user9/{*}/name", handler);
        collector.get("/user10/{**}/name", handler);

        final RouteResult r1 = dispatcher.dispatch("GET", "/user1/1");
        assertEquals(RouteStatus.FOUND, r1.getStatus());
        assertEquals("1", r1.getParams().get("id"));

        final RouteResult r2 = dispatcher.dispatch("GET", "/user2/1");
        assertEquals(RouteStatus.FOUND, r2.getStatus());
        assertEquals("1", r2.getParams().get("id"));
        final RouteResult r3 = dispatcher.dispatch("GET", "/user2");
        assertEquals(RouteStatus.FOUND, r3.getStatus());
        assertEquals("", r3.getParams().get("id"));

        final RouteResult r4 = dispatcher.dispatch("GET", "/user3/a");
        assertEquals(RouteStatus.FOUND, r4.getStatus());
        assertEquals("a", r4.getParams().get("id"));
        final RouteResult r5 = dispatcher.dispatch("GET", "/user3/1");
        assertEquals(RouteStatus.NOT_FOUND, r5.getStatus());

        final RouteResult r6 = dispatcher.dispatch("GET", "/user4/a");
        assertEquals(RouteStatus.FOUND, r6.getStatus());
        assertEquals("a", r6.getParams().get("id"));
        final RouteResult r7 = dispatcher.dispatch("GET", "/user4");
        assertEquals(RouteStatus.FOUND, r7.getStatus());
        assertEquals("", r7.getParams().get("id"));
        final RouteResult r8 = dispatcher.dispatch("GET", "/user4/1");
        assertEquals(RouteStatus.NOT_FOUND, r8.getStatus());

        final RouteResult r9 = dispatcher.dispatch("GET", "/user5/1/a");
        assertEquals(RouteStatus.FOUND, r9.getStatus());
        assertEquals("1", r9.getParams().get("id"));
        assertEquals("a", r9.getParams().get("name"));

        final RouteResult r10 = dispatcher.dispatch("GET", "/user6/name");
        assertEquals(RouteStatus.FOUND, r10.getStatus());

        final RouteResult r11 = dispatcher.dispatch("GET", "/user7/name/a");
        assertEquals(RouteStatus.FOUND, r11.getStatus());
        assertEquals("a", r11.getParams().get("name"));

        final RouteResult r12 = dispatcher.dispatch("GET", "/");
        assertEquals(RouteStatus.FOUND, r12.getStatus());

        final RouteResult r13 = dispatcher.dispatch("GET", "/user8/name");
        assertEquals(RouteStatus.FOUND, r13.getStatus());
        final RouteResult r14 = dispatcher.dispatch("GET", "/user8/nbme");
        assertEquals(RouteStatus.FOUND, r14.getStatus());

        final RouteResult r15 = dispatcher.dispatch("GET", "/user9/test/name");
        assertEquals(RouteStatus.FOUND, r15.getStatus());

        final RouteResult r16 = dispatcher.dispatch("GET", "/user10/test/name");
        assertEquals(RouteStatus.FOUND, r16.getStatus());
        final RouteResult r17 = dispatcher.dispatch(
            "GET",
            "/user10/test1/test2/name"
        );
        assertEquals(RouteStatus.FOUND, r17.getStatus());
    }

    public void handler() {}
}
