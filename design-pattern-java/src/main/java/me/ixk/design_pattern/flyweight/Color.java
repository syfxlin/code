package me.ixk.design_pattern.flyweight;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Otstar Lin
 * @date 2021/1/1 下午 3:48
 */
public class Color {

    private static final Map<String, Color> COLORS = new ConcurrentHashMap<>(
        50
    );

    private final int red;
    private final int green;
    private final int blue;

    private Color(final int red, final int green, final int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public String asString() {
        return String.format(
            "rgb(%s, %s, %s)",
            this.red,
            this.green,
            this.blue
        );
    }

    public static Color valueOf(final String rgb) {
        final Integer[] array = Arrays
            .stream(rgb.split(" "))
            .map(Integer::valueOf)
            .toArray(Integer[]::new);
        return valueOf(array[0], array[1], array[2]);
    }

    public static Color valueOf(
        final int red,
        final int green,
        final int blue
    ) {
        return COLORS.computeIfAbsent(
            String.format("%s %s %s", red, green, blue),
            k -> new Color(red, green, blue)
        );
    }
}
