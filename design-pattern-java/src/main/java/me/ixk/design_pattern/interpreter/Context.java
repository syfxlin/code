package me.ixk.design_pattern.interpreter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Otstar Lin
 * @date 2021/1/12 下午 8:38
 */
public class Context {

    private final Map<Variable, Boolean> map = new HashMap<>();

    public void assign(final Variable var, final boolean value) {
        map.put(var, value);
    }

    public boolean lookup(final Variable var) throws IllegalArgumentException {
        final Boolean value = map.get(var);
        if (value == null) {
            throw new IllegalArgumentException();
        }
        return value;
    }
}
