package me.ixk.days.day5;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import me.ixk.days.day5.annotation.Bean;
import me.ixk.days.day5.annotation.Component;
import me.ixk.days.day5.annotation.Configuration;
import org.junit.jupiter.api.Test;

/**
 * @author Otstar Lin
 * @date 2020/11/22 下午 2:02
 */
class Day5Test {

    @Test
    void component() {
        final MergedAnnotation annotation = AnnotationUtils.getAnnotation(
            Route.class
        );
        assertTrue(annotation.hasAnnotation(Bean.class));
        assertArrayEquals(
            new String[] { "route" },
            annotation.getAnnotation(Component.class).name()
        );
        assertArrayEquals(
            new String[] { "route" },
            annotation.getAnnotation(Bean.class).name()
        );
    }

    @Test
    void configuration() {
        final MergedAnnotation annotation = AnnotationUtils.getAnnotation(
            Config.class
        );
        assertTrue(annotation.hasAnnotation(Bean.class));
        assertArrayEquals(
            new String[] { "config" },
            annotation.getAnnotation(Configuration.class).name()
        );
        assertArrayEquals(
            new String[] { "config" },
            annotation.getAnnotation(Component.class).name()
        );
        assertArrayEquals(
            new String[] { "config" },
            annotation.getAnnotation(Bean.class).name()
        );
        assertEquals("test", annotation.get(Component.class, "test"));
    }
}
