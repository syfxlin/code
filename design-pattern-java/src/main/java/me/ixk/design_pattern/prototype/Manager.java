package me.ixk.design_pattern.prototype;

import java.util.HashMap;
import java.util.Map;

/**
 * 管理器
 *
 * @author Otstar Lin
 * @date 2020/12/25 上午 8:58
 */
public class Manager {

    private final Map<Class<? extends Prototype>, Prototype> map = new HashMap<>();

    public void register(final Prototype prototype) {
        this.map.put(prototype.getClass(), prototype);
    }

    @SuppressWarnings("unchecked")
    public <T> T make(final Class<? extends Prototype> prototype) {
        final Prototype o = this.map.get(prototype);
        if (o == null) {
            throw new IllegalArgumentException("Prototype not found");
        }
        return (T) o.clone();
    }
}
