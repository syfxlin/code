package me.ixk.days.day33.ioc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;
import me.ixk.days.day33.ioc.bean.AutoWiredTest;
import me.ixk.days.day33.ioc.bean.DataBinderUser;
import me.ixk.days.day33.ioc.bean.TypeUser;
import me.ixk.days.day33.ioc.bean.User;
import me.ixk.days.day33.ioc.context.ApplicationContext;
import me.ixk.days.day33.ioc.context.ScopeType;
import me.ixk.days.day33.ioc.factory.FactoryBean;
import me.ixk.days.day33.ioc.injector.DefaultMethodInjector;
import me.ixk.days.day33.ioc.injector.DefaultParameterInjector;
import me.ixk.days.day33.ioc.injector.DefaultPropertyInjector;
import me.ixk.days.day33.ioc.injector.PropertiesValueInjector;
import org.junit.jupiter.api.Test;

/**
 * @author Otstar Lin
 * @date 2020/12/24 下午 8:22
 */
class Day33Test {

    static final Container container = new Container();

    static {
        container.registerContext(new ApplicationContext());
        container.addParameterInjector(new DefaultParameterInjector());
        container.addInstanceInjector(new PropertiesValueInjector());
        container.addInstanceInjector(new DefaultPropertyInjector());
        container.addInstanceInjector(new DefaultMethodInjector());
        container.instance(new User("syfxlin", 20));
    }

    @Test
    void alias() {
        container.setAlias("user", "User");
        assertNotNull(container.make("user", User.class));

        assertTrue(container.hasAlias("user"));

        assertEquals("User", container.getAlias("user"));

        container.removeAlias("user");

        assertNull(container.getAlias("user"));
    }

    @Test
    void attribute() {
        container.setAttribute("attr", "value", ScopeType.SINGLETON.asString());
        assertEquals("value", container.getAttribute("attr"));

        assertTrue(container.hasAttribute("attr"));

        container.removeAttribute("attr");

        assertFalse(container.hasAttribute("attr"));
    }

    @Test
    void bind() {
        // name, type
        container.bind("test1", AutoWiredTest.class);
        final AutoWiredTest test1 = container.make(
            "test1",
            AutoWiredTest.class
        );
        assertEquals("syfxlin", test1.getUser().getName());

        // name, factory
        container.bind(
            "test2",
            new FactoryBean<String>() {
                @Override
                public String getObject() throws Exception {
                    return "value";
                }

                @Override
                public Class<?> getObjectType() {
                    return String.class;
                }
            }
        );
        final String test2 = container.make("test2", String.class);
        assertEquals("value", test2);

        // type
        container.bind(TypeUser.class);
        final TypeUser typeUser = container.make(TypeUser.class);
        assertEquals("syfxlin", typeUser.getUser().getName());

        // factory
        container.bind(
            new FactoryBean<Integer>() {
                @Override
                public Integer getObject() throws Exception {
                    return 1;
                }

                @Override
                public Class<?> getObjectType() {
                    return Integer.class;
                }
            }
        );
        final Integer factoryInteger = container.make(Integer.class);
        assertEquals(1, factoryInteger);
    }

    @Test
    void instance() {
        container.instance("user1", new User("syfxlin", 20));
        assertEquals("syfxlin", container.make("user1", User.class).getName());

        assertEquals("syfxlin", container.make("User", User.class).getName());
    }

    @Test
    void make() {
        assertEquals("syfxlin", container.make(User.class).getName());
        assertEquals("syfxlin", container.make("User", User.class).getName());

        assertEquals(
            "syfxlin",
            container
                .make(
                    DataBinderUser.class,
                    new DefaultDataBinder(container, Map.of("name", "syfxlin"))
                )
                .getName()
        );
    }

    @Test
    void remove() {
        container.bind("remove1", AutoWiredTest.class);
        assertTrue(container.has("remove1"));
        container.remove("remove1");
        assertFalse(container.has("remove1"));
        assertFalse(
            container.getBindingNamesByType().get(AutoWiredTest.class).isEmpty()
        );
    }

    @Test
    void call() throws NoSuchMethodException {
        final User result1 = container.call(
            this,
            this.getClass().getMethod("method1", User.class),
            User.class
        );
        assertEquals("syfxlin", result1.getName());

        final String result2 = container.call("User", "getName", String.class);
        assertEquals("syfxlin", result2);

        final String result3 = container.call(
            User.class,
            "getName",
            String.class
        );
        assertEquals("syfxlin", result3);

        assertEquals(
            "syfxlin",
            container.call(User.class.getMethod("getName"))
        );

        assertEquals(
            "syfxlin",
            container.call(this, "method1", User.class).getName()
        );
    }

    public User method1(User user) {
        return user;
    }
}
