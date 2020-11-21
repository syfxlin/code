package me.ixk.days.day4;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Method;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * @author Otstar Lin
 * @date 2020/11/21 下午 2:20
 */
class Day4Test {
    private static Method METHOD;
    private static final String[] PARAMETER_REF_NAMES = new String[] {
        "arg0",
        "arg1",
    };
    private static final String[] PARAMETER_NAMES = new String[] {
        "name",
        "age",
    };

    @BeforeAll
    static void setup() {
        try {
            METHOD =
                me.ixk.days.day4.Test.class.getMethod(
                        "test",
                        String.class,
                        String.class
                    );
        } catch (final NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getParameterNames() {
        assertArrayEquals(
            PARAMETER_NAMES,
            ParameterNameDiscoverer.getParameterNames(METHOD)
        );
    }

    @Test
    void getParameterNamesByReflection() {
        final String[] names = ParameterNameDiscoverer.getParameterNamesByReflection(
            METHOD
        );
        assertTrue(
            Arrays.equals(PARAMETER_NAMES, names) ||
            Arrays.equals(PARAMETER_REF_NAMES, names)
        );
    }

    @Test
    void getParameterNamesByAsm() {
        assertArrayEquals(
            PARAMETER_NAMES,
            ParameterNameDiscoverer.getParameterNamesByAsm(METHOD)
        );
    }
}
