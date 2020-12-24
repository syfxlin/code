/*
 * Copyright (c) 2020, Otstar Lin (syfxlin@gmail.com). All Rights Reserved.
 */

package me.ixk.days.day33.property;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 配置
 *
 * @author Otstar Lin
 * @date 2020/12/21 下午 9:28
 */
public class Environment extends CompositePropertySource {

    public static final String CONFIG_LOCATION_NAME = "xkjava.config.location";
    public static final String CONFIG_ACTIVE_NAME = "xkjava.config.active";
    public static final String DEFAULT_CONFIG_LOCATION = "classpath:/";
    public static final String DEFAULT_CONFIG_NAME = "";

    private final Set<String> activeProfiles = new LinkedHashSet<>();

    public Environment(String name) {
        super(name);
    }

    public Environment(String name, List<PropertySource<?>> source) {
        super(name, source);
    }

    public Map<String, PropertySource<?>> all() {
        return this.getSource();
    }

    public Set<String> getActiveProfiles() {
        synchronized (this.activeProfiles) {
            if (this.activeProfiles.isEmpty()) {
                final String profiles =
                    this.get(CONFIG_ACTIVE_NAME, String.class);
                if (profiles != null && !profiles.isEmpty()) {
                    setActiveProfiles(profiles.trim().split(","));
                }
            }
            return this.activeProfiles;
        }
    }

    public void setActiveProfiles(String... profiles) {
        synchronized (this.activeProfiles) {
            this.activeProfiles.clear();
            for (String profile : profiles) {
                profile = profile.trim();
                if (profile.isEmpty()) {
                    throw new IllegalArgumentException(
                        "Invalid profile [" + profile + "]: must contain text"
                    );
                }
                this.activeProfiles.add(profile);
            }
        }
    }

    public void addActiveProfile(String profile) {
        profile = profile.trim();
        if (profile.isEmpty()) {
            throw new IllegalArgumentException(
                "Invalid profile [" + profile + "]: must contain text"
            );
        }
        this.getActiveProfiles();
        synchronized (this.activeProfiles) {
            this.activeProfiles.add(profile);
        }
    }
}
